package be.kdg.ip3.archportal.games.shared;

import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
public record GlobalGameDto(
        UUID id,
        UUID studioId,
        String title,
        String description,
        String imageUrl,
        String gameUrl,
        BigDecimal price,
        GameGenre genre,
        int maxlobbysize
) {
    public static GlobalGameDto fromDomain(Game game) {
        return new GlobalGameDto(game.getId().id(), game.getStudioId().id(), game.getTitle(), game.getDescription(),
                game.getImageUrl(), game.getGameUrl(), game.getPrice().money(), game.getGenre(),game.getMaxLobbySize());
    }
}