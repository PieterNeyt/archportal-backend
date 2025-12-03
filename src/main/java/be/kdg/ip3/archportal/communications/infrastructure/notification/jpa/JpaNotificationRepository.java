package be.kdg.ip3.archportal.communications.infrastructure.notification.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaNotificationRepository extends JpaRepository<JpaNotificationEntity, UUID> {

}
