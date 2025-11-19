package be.kdg.ip3.archportal.gameService.domain.gamestudio;

import org.springframework.util.Assert;

import java.util.UUID;

public record GameStudioId(UUID id) {
    public GameStudioId {
        Assert.notNull(id, "id is null");
    }
    public static GameStudioId create() {
        return new GameStudioId(UUID.randomUUID());
    }

}
