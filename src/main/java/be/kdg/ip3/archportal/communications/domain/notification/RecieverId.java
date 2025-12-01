package be.kdg.ip3.archportal.communications.domain.notification;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record RecieverId(UUID id) {
    public RecieverId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Profile [" + id + "] not found");
    }

    public static RecieverId create() {
        return new RecieverId(UUID.randomUUID());
    }
}
