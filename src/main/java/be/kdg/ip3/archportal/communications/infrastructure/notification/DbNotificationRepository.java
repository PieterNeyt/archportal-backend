package be.kdg.ip3.archportal.communications.infrastructure.notification;

import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationRepository;
import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationEntity;
import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbNotificationRepository implements NotificationRepository {
    private final JpaNotificationRepository repository;
    public DbNotificationRepository(JpaNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Notification newNotification) {
        var jpaNotification = JpaNotificationEntity.fromDomain(newNotification);
        this.repository.save(jpaNotification);
    }
}
