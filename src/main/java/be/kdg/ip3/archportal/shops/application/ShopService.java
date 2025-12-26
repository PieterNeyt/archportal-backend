package be.kdg.ip3.archportal.shops.application;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformPointsEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.api.dto.BenefitDto;
import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;
import be.kdg.ip3.archportal.shops.domain.NotFoundException;
import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitRepository;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.mollie.IMollieService;
import be.kdg.ip3.archportal.shops.domain.order.Order;
import be.kdg.ip3.archportal.shops.domain.order.OrderLine;
import be.kdg.ip3.archportal.shops.domain.order.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ShopService {
    private final GamesApi gameApi;
    private final ProfilesApi profilesApi;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final BenefitRepository benefitRepo;
    private final IMollieService mollieService;
    private final ApplicationEventPublisher publisher;
    private final int platformPointsMultiplier;

    public ShopService(GamesApi gameApi, ProfilesApi profilesApi, CartRepository cartRepo, OrderRepository orderRepo, BenefitRepository benefitRepo, IMollieService mollieService, ApplicationEventPublisher publisher, @Value("${shop.platformPointsMultiplier}") int platformPointsMultiplier) {
        this.gameApi = gameApi;
        this.profilesApi = profilesApi;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.benefitRepo = benefitRepo;
        this.mollieService = mollieService;
        this.publisher = publisher;
        this.platformPointsMultiplier = platformPointsMultiplier;
    }

    public List<GlobalGameDto> getGamesForCart(Cart cart) {
        return gameApi.getGamesByIds(cart.getCartItems());
    }

    public List<GlobalGameDto> getAllGames() {
        return gameApi.getAllGames();
    }


    public Cart getOrCreateCart(UUID profileId) {
        return cartRepo.findByProfileId(profileId)
                .orElseGet(() -> cartRepo.save(new Cart(profileId)));
    }

    public Cart addToCart(UUID profileId, UUID gameId) {
        var cart = getOrCreateCart(profileId);
        profilesApi.checkAlreadyOwnsGame(profileId, gameId);
        if (cart.getCartItems().contains(gameId)) {
            throw new IllegalArgumentException("Game already added to cart.");
        }
        cart.addToCart(gameId);
        return cartRepo.save(cart);
    }

    public Cart removeFromCart(UUID profileId, UUID gameId) {

        var cart = cartRepo.findByProfileId(profileId)
                .orElseThrow(() -> new NotFoundException("No cart found for user with id: " + profileId));

        if (!cart.getCartItems().contains(gameId)) {
            throw new IllegalArgumentException("Game is not in cart");
        }

        cart.removeFromCart(gameId);

        return cartRepo.save(cart);
    }


    public PaymentCreationDto checkout(UUID profileId) {

        var cart = getOrCreateCart(profileId);
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        var gamesInCart = gameApi.getGamesByIds(cart.getCartItems());

        var order = new Order(profileId);

        gamesInCart.forEach(gameDto -> order.addOrderLine(gameDto.id(), gameDto.price()));
        orderRepo.save(order);

        var amount = order.totalPrice();
        PaymentCreationDto payment = mollieService.createPayment(
                amount,
                "Order #" + order.getOrderId().id(),
                order.getOrderId().id()
        );

        order.attachPayment(payment.paymentId());
        orderRepo.save(order);

        cartRepo.delete(cart);
        return payment;
    }


    public boolean verifyPayment(UUID orderId) {
        var order = orderRepo.findById(orderId);
        boolean success = mollieService.verifyPayment(order.getPaymentId());

        if (success && !order.isCompleted()) {
            var gameIds = order.getOrderLines()
                    .stream()
                    .map(OrderLine::getGameId)
                    .toList();

            profilesApi.addGamesToLibrary(order.getProfileId(), gameIds);
            order.markAsCompleted();
            orderRepo.save(order);

            publisher.publishEvent(new GrantPlatformPointsEvent(
                    order.getProfileId(),
                    order.totalPrice()
                            .multiply(new BigDecimal(platformPointsMultiplier))
                            .setScale(0, RoundingMode.HALF_UP)
                            .intValue()
            ));

        }

        return success;
    }

    public GlobalGameDto getGame(UUID gameId) {
        return gameApi.getGameById(gameId);
    }

    //benefits

    public List<BenefitDto> getAllBenefits() {
        return benefitRepo.findAll()
                .stream()
                .map(BenefitDto::fromDomain)
                .toList();
    }

    public int buyBenefit(UUID profileId, BenefitId benefitId) {
        Benefit benefit = benefitRepo.findById(benefitId).orElseThrow(benefitId::notFound);

        return profilesApi.addBenefitToProfile(profileId,benefit.getBenefitId().id(),benefit.getPointCost());
    }

    public BenefitDto getBenefit(BenefitId id) {
        return benefitRepo.findById(id)
                .map(BenefitDto::fromDomain)
                .orElseThrow(id::notFound);
    }



}
