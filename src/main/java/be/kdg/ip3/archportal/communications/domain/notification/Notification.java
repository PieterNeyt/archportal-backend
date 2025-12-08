package be.kdg.ip3.archportal.communications.domain.notification;

import be.kdg.ip3.archportal.communications.shared.NotificationType;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.time.Instant;
import java.util.Date;

@Entity
@Getter
public class Notification {
    private final NotificationId id;
    private final ReceiverId receiverId;
    private final String title;
    private final String body;
    private final NotificationType type;
    private final Date createdAt;

    public Notification(Date createdAt, NotificationType type, String body, String title, ReceiverId receiverId, NotificationId id) {
        this.createdAt = createdAt;
        this.type = type;
        this.body = body;
        this.title = title;
        this.receiverId = receiverId;
        this.id = id;
    }
    public Notification(NotificationType type, String body, String title, ReceiverId receiverId) {
        this(Date.from(Instant.now()),type,body,title, receiverId, NotificationId.create());
    }


    public void checkReciever(ReceiverId receiverId) {
        if(!this.receiverId.equals(receiverId))
            throw new IllegalArgumentException("reciever id not match");
    }
}
