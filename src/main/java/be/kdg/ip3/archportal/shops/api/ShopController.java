package be.kdg.ip3.archportal.shops.api;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.shops.api.dto.BenefitDto;
import be.kdg.ip3.archportal.shops.api.dto.CartDto;
import be.kdg.ip3.archportal.shops.api.dto.PaymentCreationDto;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @GetMapping("/game/{id}")
    public ResponseEntity<GlobalGameDto> getGame(@PathVariable("id") UUID gameId) {
        var game = shopService.getGame(gameId);
        return ResponseEntity.ok(game);
    }


    @GetMapping("/cart")
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal Jwt token) {
        Cart cart = shopService.getOrCreateCart(UUID.fromString(token.getSubject()));
        List<GlobalGameDto> games = shopService.getGamesForCart(cart);
        return ResponseEntity.ok(CartDto.from(cart, games));
    }

    @PutMapping("/cart/add")
    public ResponseEntity<CartDto> addToCart(@RequestParam UUID gameId, @AuthenticationPrincipal Jwt token) {
        Cart cart = shopService.addToCart(UUID.fromString(token.getSubject()), gameId);
        List<GlobalGameDto> games = shopService.getGamesForCart(cart);
        return ResponseEntity.ok(CartDto.from(cart, games));
    }

    @PutMapping("/cart/remove")
    public ResponseEntity<CartDto> removeFromCart(
            @RequestParam UUID gameId,
            @AuthenticationPrincipal Jwt token) {
        Cart cart = shopService.removeFromCart(UUID.fromString(token.getSubject()), gameId);
        List<GlobalGameDto> games = shopService.getGamesForCart(cart);
        return ResponseEntity.ok(CartDto.from(cart, games));
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentCreationDto> checkout(
            @RequestParam(required = false) UUID benefitId,
            @AuthenticationPrincipal Jwt token) {

        BenefitId id = (benefitId != null) ? new BenefitId(benefitId) : null;
        PaymentCreationDto payment = shopService.checkout(UUID.fromString(token.getSubject()), id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/payment/verify")
    public ResponseEntity<Boolean> verifyPayment(@RequestParam UUID orderId) {
        boolean verified = shopService.verifyPayment(orderId);
        return ResponseEntity.ok(verified);
    }


    // benefits

    @GetMapping("/benefits")
    public ResponseEntity<List<BenefitDto>> getAllBenefits() {
        return ResponseEntity.ok(shopService.getAllBenefits());
    }

    @PostMapping("/benefits/{benefitId}/buy")
    public ResponseEntity<Integer> buyBenefit(
            @PathVariable UUID benefitId,
            @AuthenticationPrincipal Jwt token) {

        var profileId = UUID.fromString(token.getSubject());
        var newPointTotal = shopService.buyBenefit(profileId, new BenefitId(benefitId));

        return ResponseEntity.ok(newPointTotal);
    }

    @GetMapping("/benefits/{id}")
    public ResponseEntity<BenefitDto> getBenefit(@PathVariable UUID id) {
        return ResponseEntity.ok(shopService.getBenefit(new BenefitId(id)));
    }

    @GetMapping("/benefits/discounts")
    public ResponseEntity<List<BenefitDto>> getProfileDiscounts(@AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        return ResponseEntity.ok(shopService.getProfileDiscounts(profileId));
    }

    @GetMapping("/benefits/active-color")
    public ResponseEntity<String> getActiveUsernameColor(@AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        return ResponseEntity.ok(shopService.getActiveUsernameColor(profileId));
    }

    @GetMapping("/benefits/active-color/profile/{profileId}")
    public ResponseEntity<String> getActiveUsernameColorFromProfileId(@PathVariable UUID profileId) {;
        return ResponseEntity.ok(shopService.getActiveUsernameColor(profileId));
    }


    @GetMapping("/benefits/profile")
    public ResponseEntity<List<BenefitDto>> getAllBenefitsOfProfile(@AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        return ResponseEntity.ok(shopService.getBenefitsOfProfile(profileId).stream().map(BenefitDto::fromDomain).toList());
    }
}
