package be.kdg.ip3.archportal.communications.infrastructure.notificationsettings;

import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.jpa.JpaNotificationSettingsEntity;
import be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.jpa.JpaNotificationSettingsRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DbNotificationSettingsRepository implements NotificationSettingsRepository {
    private final JpaNotificationSettingsRepository repository;
    public DbNotificationSettingsRepository(JpaNotificationSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<NotificationSettings> findById(ProfileId profileId) {
        return this.repository.findById(profileId.id()).map(JpaNotificationSettingsEntity::toDomain);
    }

    @Override
    public void save(NotificationSettings settings) {
        var jpaSettings = JpaNotificationSettingsEntity.fromDomain(settings);
        this.repository.save(jpaSettings);
    }
}
