package be.kdg.ip3.archportal.communications.infrastructure.notification.jpa;


import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationId;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.communications.domain.notification.ReceiverId;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "communicationservice")
public class JpaNotificationEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID receiverId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    public JpaNotificationEntity(UUID id, UUID receiverId, String title, String body, NotificationType type, Date createdAt) {
        this.id = id;
        this.receiverId = receiverId;
        this.title = title;
        this.body = body;
        this.type = type;
        this.createdAt = createdAt;
    }

    public JpaNotificationEntity() {
    }

    public static JpaNotificationEntity fromDomain(Notification domain) {
        return new JpaNotificationEntity(
                domain.getId().id(),
                domain.getReceiverId().id(),
                domain.getTitle(),
                domain.getBody(),
                domain.getType(),
                domain.getCreatedAt()
        );
    }

    public Notification toDomain() {
        return new Notification(
                this.createdAt,
                this.type,
                this.body,
                this.title,
                new ReceiverId(this.receiverId),
                new NotificationId(this.id)
        );
    }
}
