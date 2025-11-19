package be.kdg.ip3.archportal.gameService.api.dto;

import be.kdg.ip3.archportal.gameService.domain.game.Game;
import be.kdg.ip3.archportal.gameService.domain.game.GameGenre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record GameDto(
        UUID id,
        @NotNull
        UUID studioId,
        @Size(min = 1, max = 100)
        String title,
        @Size(min = 1, max = 255)
        String description,
        String imageUrl,
        @Min(0)
        BigDecimal price,
        @NotNull
        GameGenre genre
) {
    public static GameDto fromDomain(Game game) {
        return new GameDto(game.getId().id(), game.getStudioId().id(), game.getTitle(), game.getDescription(),
                game.getImageUrl(), game.getPrice().money(), game.getGenre());
    }
}
