package be.kdg.ip3.archportal.communications.domain.chatroom;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDateTime;
import java.util.UUID;

@ValueObject
public record Message(MessageId id, UUID sender, String text, LocalDateTime timestamp) {
}
