package be.kdg.ip3.archportal.communications.domain.chatroom;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record MessageId(UUID id) {
    public MessageId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Message [" + id + "] not found");
    }
}
