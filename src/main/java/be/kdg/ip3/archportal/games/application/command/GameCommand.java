package be.kdg.ip3.archportal.games.application.command;

import be.kdg.ip3.archportal.games.api.dto.GameDto;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import be.kdg.ip3.archportal.games.domain.game.GameId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.infrastructure.messaging.config.RegisterGameMessage;

import java.math.BigDecimal;
import java.util.UUID;

public record GameCommand(
        UUID id,
        String title,
        String description,
        String imageUrl,
        String gameUrl,
        BigDecimal price,
        GameGenre genre,
        int maxlobbysize
) {
    public static GameCommand fromDto(GameDto gameDto) {
        return new GameCommand(
                gameDto.id(),
                gameDto.title(),
                gameDto.description(),
                gameDto.imageUrl(),
                gameDto.gameUrl(),
                gameDto.price(),
                gameDto.genre(),
                gameDto.maxlobbysize()
        );
    }
    
    public static GameCommand fromMessage(RegisterGameMessage message) {
        return new GameCommand(
                null,
                message.title(),
                message.description(),
                message.imageUrl(),
                message.gameUrl(),
                message.price(),
                message.genre(),
                message.maxlobbysize()
        );
    }

    public Game toDomain(GameStudioId gameStudioId) {
        GameId gameId;
        if (id == null) {
            gameId = GameId.create();
        } else {
            gameId = new GameId(id);
        }
        return new Game(
                gameId,
                gameStudioId,
                title,
                description,
                price,
                imageUrl,
                gameUrl,
                genre,
                maxlobbysize
        );
    }
}

