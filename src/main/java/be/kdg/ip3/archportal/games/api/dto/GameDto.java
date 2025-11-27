package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record GameDto(
        UUID id,
        @Size(min = 1, max = 100)
        @NotNull
        String title,
        @Size(min = 1, max = 255)
        @NotNull
        String description,
        String imageUrl,
        @NotNull
        @Size(min = 5)
        String gameUrl,
        @Min(0)
        BigDecimal price,
        @NotNull
        GameGenre genre
) {
    public static GameDto fromDomain(Game game) {
        return new GameDto(game.getId().id(), game.getTitle(), game.getDescription(),
                game.getImageUrl(), game.getGameUrl(), game.getPrice().money(), game.getGenre());
    }
}
