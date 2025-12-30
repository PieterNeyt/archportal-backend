package be.kdg.ip3.archportal.games.infrastructure.messaging.config;

import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RegisterGameMessage(
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
        GameGenre genre,
        @NotNull
        @Min(1)
        int maxlobbysize
) {
}
