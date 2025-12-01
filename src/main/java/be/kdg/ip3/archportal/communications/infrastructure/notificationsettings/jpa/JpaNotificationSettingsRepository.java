package be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaNotificationSettingsRepository extends JpaRepository<JpaNotificationSettingsEntity, UUID> {

}
