package be.kdg.ip3.archportal.communications.domain.notification;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record ReceiverId(UUID id) {
    public ReceiverId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("ReceiverId [" + id + "] not found");
    }

    public static ReceiverId create() {
        return new ReceiverId(UUID.randomUUID());
    }
}
