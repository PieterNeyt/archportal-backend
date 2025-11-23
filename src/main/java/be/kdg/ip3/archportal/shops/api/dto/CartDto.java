package be.kdg.ip3.archportal.shops.api.dto;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartDto(
        UUID cartId,
        UUID profileId,
        List<GlobalGameDto> items, // Gewijzigd van List<UUID> naar List<GameDto>
        double totalPrice    // Toegevoegd
) {
    public static CartDto from(Cart cart, List<GlobalGameDto> games) {
        double totalPrice = games.stream()
                .map(GlobalGameDto::price)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        return new CartDto(
                cart.getCartId().id(),
                cart.getProfileId(),
                games,
                totalPrice
        );
    }
}