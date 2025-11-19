package be.kdg.ip3.archportal.gameService.domain.update;

import org.springframework.util.Assert;

import java.util.UUID;

public record UpdateId(UUID id) {

    public UpdateId {
        Assert.notNull(id, "id is null");
    }

    public static UpdateId create() {
        return new UpdateId(UUID.randomUUID());
    }
}
