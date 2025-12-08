package be.kdg.ip3.archportal.communications.domain.notification;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository {
    void save(Notification newNotification);

    Optional<List<Notification>> findByReceiverId(ReceiverId receiverId);

    Optional<List<Notification>> findByReceiverIdFirstAmount(ReceiverId receiverId, int amount);

    int getTotalNotificationFromReceiverId(ReceiverId receiverId);

    Optional<Notification> findById(NotificationId notificationId);

    void delete(Notification notification);

}
