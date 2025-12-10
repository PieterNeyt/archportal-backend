package be.kdg.ip3.archportal.communications.domain.chatroom;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Message {
    private final MessageId id;
    private final UUID senderId;
    private String text;
    private final LocalDateTime timestamp;

    public Message(MessageId id, UUID senderId, String text, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Message(UUID senderId, String text) {
        this.id = new MessageId(UUID.randomUUID());
        this.senderId = senderId;
        setText(text);
        this.timestamp = LocalDateTime.now();
    }

    private void setText(String text) {
        if (text == null || text.isBlank())
            throw new NullPointerException("Message is invalid");
        if (text.length() > 500)
            throw new IllegalArgumentException("Message is too long");
        this.text = text;
    }
}
