package be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa;

import be.kdg.ip3.archportal.communications.domain.chatroom.Message;
import be.kdg.ip3.archportal.communications.domain.chatroom.MessageId;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message", schema = "communicationservice")
public class JpaMessageEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID sender;
    @Column(nullable = false, length = 500)
    private String text;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private JpaChatRoomEntity chatRoom;

    protected JpaMessageEntity() {
    }

    public JpaMessageEntity(UUID id, UUID sender, String text, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    public static JpaMessageEntity fromDomain(Message message) {
        return new JpaMessageEntity(
                message.id().id(),
                message.sender(),
                message.text(),
                message.timestamp()
        );
    }

    public Message toDomain() {
        return new Message(
                new MessageId(id),
                sender,
                text,
                timestamp
        );
    }
}
