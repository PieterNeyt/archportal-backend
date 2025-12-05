package be.kdg.ip3.archportal.communications.infrastructure.notification.jpa;


import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationId;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.communications.domain.notification.RecieverId;
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

    @Column(nullable = false)
    private boolean isRead;

    public JpaNotificationEntity(UUID id, UUID receiverId, String title, String body, NotificationType type, Date createdAt, boolean isRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.title = title;
        this.body = body;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public JpaNotificationEntity() {
    }

    public static JpaNotificationEntity fromDomain(Notification domain) {
        return new JpaNotificationEntity(
                domain.getId().id(),
                domain.getRecieverId().id(),
                domain.getTitle(),
                domain.getBody(),
                domain.getType(),
                domain.getCreatedAt(),
                domain.isRead()
        );
    }

    public Notification toDomain() {
        return new Notification(
                this.isRead,
                this.createdAt,
                this.type,
                this.body,
                this.title,
                new RecieverId(this.receiverId),
                new NotificationId(this.id)
        );
    }
}
