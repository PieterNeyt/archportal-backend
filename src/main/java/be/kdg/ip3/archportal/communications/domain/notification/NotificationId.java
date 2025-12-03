package be.kdg.ip3.archportal.communications.domain.notification;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record NotificationId(UUID id) {
    public NotificationId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Notificaiton [" + id + "] not found");
    }

    public static NotificationId create() {
        return new NotificationId(UUID.randomUUID());
    }
}
