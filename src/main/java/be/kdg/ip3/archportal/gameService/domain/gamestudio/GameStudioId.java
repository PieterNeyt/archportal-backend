package be.kdg.ip3.archportal.gameService.domain.gamestudio;

import be.kdg.ip3.archportal.gameService.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record GameStudioId(UUID id) {
    public GameStudioId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Game studio [" + id + "] not found");
    }

    public static GameStudioId create() {
        return new GameStudioId(UUID.randomUUID());
    }

}
