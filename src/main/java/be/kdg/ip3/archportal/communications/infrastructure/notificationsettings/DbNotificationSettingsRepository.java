package be.kdg.ip3.archportal.communications.infrastructure.notificationsettings;

import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.jpa.JpaNotificationSettingsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbNotificationSettingsRepository implements NotificationSettingsRepository {
    private final JpaNotificationSettingsRepository repository;
    public DbNotificationSettingsRepository(JpaNotificationSettingsRepository repository) {
        this.repository = repository;
    }

}
