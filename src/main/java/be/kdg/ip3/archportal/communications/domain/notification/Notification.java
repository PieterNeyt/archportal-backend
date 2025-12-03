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
    private final RecieverId recieverId;
    private final String title;
    private final String body;
    private final NotificationType type;
    private final Date createdAt;
    private boolean isRead;

    public Notification(boolean isRead, Date createdAt, NotificationType type, String body, String title, RecieverId recieverId, NotificationId id) {
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.type = type;
        this.body = body;
        this.title = title;
        this.recieverId = recieverId;
        this.id = id;
    }
    public Notification(NotificationType type, String body, String title, RecieverId recieverId) {
        this(false,Date.from(Instant.now()),type,body,title,recieverId, NotificationId.create());
    }

    public void read(){
        this.isRead = true;
    }

}
