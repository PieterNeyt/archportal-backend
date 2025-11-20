package be.kdg.ip3.archportal.games.domain.game;

import be.kdg.ip3.archportal.gameService.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record GameId(UUID id) {
    public GameId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Game [" + id + "] not found");
    }

    public static GameId create() {
        return new GameId(UUID.randomUUID());
    }

}
