package be.kdg.ip3.archportal.shops.application;

import be.kdg.ip3.archportal.games.shared.GameDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.mollie.IMollieService;
import be.kdg.ip3.archportal.shops.domain.order.Order;
import be.kdg.ip3.archportal.shops.domain.order.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ShopService {
    private final GamesApi gameApi;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final IMollieService mollieService;

    public ShopService(GamesApi gameApi, CartRepository cartRepo, OrderRepository orderRepo, IMollieService mollieService) {
        this.gameApi = gameApi;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.mollieService = mollieService;
    }
    public List<GameDto> getGamesForCart(Cart cart) {
        return gameApi.getGamesByIds(cart.getCartItems());
    }
    public List<GameDto> getAllGames() {
        return gameApi.getAllGames();
    }
    public Cart getOrCreateCart(UUID profileId) {
        return cartRepo.findByProfileId(profileId)
                .orElseGet(() -> cartRepo.save(new Cart(profileId)));
    }

    public Cart addToCart(UUID profileId, UUID gameId) {
        Cart cart = getOrCreateCart(profileId);
        if (cart.getCartItems().contains(gameId)) {
            throw new RuntimeException("Game with id " + gameId + " already added");
        }
        cart.addToCart(gameId);
        return cartRepo.save(cart);
    }

    public Cart removeFromCart(UUID profileId, UUID gameId) {
        Cart cart = getOrCreateCart(profileId);
        cart.removeFromCart(gameId);
        return cartRepo.save(cart);
    }

    public PaymentCreationDto checkout(UUID profileId) {

        Cart cart = getOrCreateCart(profileId);

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order(profileId);
        var allGames = getAllGames();

        cart.getCartItems().forEach(gameId -> {
            var game = allGames.stream()
                    .filter(g -> g.id().equals(gameId))
                    .findFirst()
                    .orElseThrow();

            order.addOrderLine(game.id(), game.price());
        });

        orderRepo.save(order);
        var amount = order.totalPrice();

        PaymentCreationDto payment = mollieService.createPayment(
                amount,
                "Order #" + order.getOrderId().id(),
                order.getOrderId().id()
        );

        order.createPayment(payment.paymentId());
        orderRepo.save(order);

        cart.getCartItems().clear();
        cartRepo.save(cart);
        return payment;
    }
    public boolean verifyPayment(UUID orderId) {
        Order order = orderRepo.findById(orderId);
        // Verifieer met Mollie
        return mollieService.verifyPayment(order.getPaymentId());
    }
}
