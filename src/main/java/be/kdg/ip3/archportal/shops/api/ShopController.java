package be.kdg.ip3.archportal.shops.api;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.shops.api.dto.CartDto;
import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/games")
    public ResponseEntity<List<GlobalGameDto>> getAllGames() {
        var games = shopService.getAllGames();
        return ResponseEntity.ok(games);
    }
    @GetMapping("/cart")
    public ResponseEntity<CartDto> getCart(@RequestParam UUID profileId) {
        Cart cart = shopService.getOrCreateCart(profileId);
        List<GlobalGameDto> games = shopService.getGamesForCart(cart);
        return ResponseEntity.ok(CartDto.from(cart, games));
    }

    @PutMapping("/cart/add")
    public ResponseEntity<CartDto> addToCart(
            @RequestParam UUID profileId,
            @RequestParam UUID gameId) {

        Cart cart = shopService.addToCart(profileId, gameId);
        List<GlobalGameDto> games = shopService.getGamesForCart(cart);
        return ResponseEntity.ok(CartDto.from(cart, games));
    }

    @PutMapping("/cart/remove")
    public ResponseEntity<CartDto> removeFromCart(
            @RequestParam UUID profileId,
            @RequestParam UUID gameId) {

        Cart cart = shopService.removeFromCart(profileId, gameId);
        List<GlobalGameDto> games = shopService.getGamesForCart(cart);
        return ResponseEntity.ok(CartDto.from(cart, games));
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentCreationDto> checkout(
            @RequestParam UUID profileId) {

        PaymentCreationDto payment = shopService.checkout(profileId);
        return ResponseEntity.ok(payment);
    }
    @GetMapping("/payment/verify")
    public ResponseEntity<Map<String, Boolean>> verifyPayment(
            @RequestParam UUID orderId) {

        boolean verified = shopService.verifyPayment(orderId);
        return ResponseEntity.ok(Map.of("verified", verified, "success", verified));
    }
}
