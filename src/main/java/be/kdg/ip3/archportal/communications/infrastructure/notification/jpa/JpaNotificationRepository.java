package be.kdg.ip3.archportal.communications.infrastructure.notification.jpa;

import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaNotificationRepository extends JpaRepository<JpaNotificationEntity, UUID> {
    int countByReceiverId(UUID receiverId);

    Optional<List<JpaNotificationEntity>>  findByReceiverIdOrderByCreatedAtDesc(UUID receiverId, Pageable pageable);

    Optional<List<JpaNotificationEntity>>  findByReceiverIdOrderByCreatedAtDesc(UUID receiverId);

}
