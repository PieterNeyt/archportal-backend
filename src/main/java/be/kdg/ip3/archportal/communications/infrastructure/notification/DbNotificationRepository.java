package be.kdg.ip3.archportal.communications.infrastructure.notification;

import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbNotificationRepository {
    private final JpaNotificationRepository repository;
    public DbNotificationRepository(JpaNotificationRepository repository) {
        this.repository = repository;
    }

}
