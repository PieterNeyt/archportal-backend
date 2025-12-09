package be.kdg.ip3.archportal.communications.domain.chatroom;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDateTime;
import java.util.UUID;

@ValueObject
public record Message(MessageId id, UUID sender, String text, LocalDateTime timestamp) {
    public static Message createMessage(UUID sender, String text) {
        return new Message(new MessageId(UUID.randomUUID()), sender, text, LocalDateTime.now());
    }
}
