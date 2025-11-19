package be.kdg.ip3.archportal.gameService.domain.gamestudio;

import org.springframework.util.Assert;

import java.util.UUID;

public record OwnerId(UUID id) {
    public OwnerId {
        Assert.notNull(id, "id is null");
    }
    public static OwnerId create() {
        return new OwnerId(UUID.randomUUID());
    }

}
