package be.kdg.ip3.archportal.games.shared;

import be.kdg.ip3.archportal.games.api.dto.AchievementDto;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
public record GlobalGameDto(
        UUID id,
        String title,
        String description,
        String imageUrl,
        String gameUrl,
        BigDecimal price,
        GameGenre genre,
        int maxlobbysize,
        List<AchievementDto> achievements
) {
    public static GlobalGameDto fromDomain(Game game) {
        return new GlobalGameDto(
                game.getId().id(),
                game.getTitle(),
                game.getDescription(),
                game.getImageUrl(),
                game.getGameUrl(),
                game.getPrice().money(),
                game.getGenre(),
                game.getMaxLobbySize(),
                game.getAchievements().stream()
                        .map(AchievementDto::fromDomain)
                        .toList()
        );
    }
}