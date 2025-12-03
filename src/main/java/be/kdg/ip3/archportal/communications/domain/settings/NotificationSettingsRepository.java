package be.kdg.ip3.archportal.communications.domain.settings;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository {
    Optional<NotificationSettings> findById(SettingId settingId);

    void save(NotificationSettings settings);
}
