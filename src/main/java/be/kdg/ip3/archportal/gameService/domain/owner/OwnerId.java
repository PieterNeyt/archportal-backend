package be.kdg.ip3.archportal.gameService.domain.owner;

import be.kdg.ip3.archportal.gameService.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record OwnerId(UUID id) {
    public OwnerId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Owner [" + id + "] not found");
    }

    public static OwnerId create() {
        return new OwnerId(UUID.randomUUID());
    }

}
