package be.kdg.ip3.archportal.communications.domain.notification;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository {
    void save(Notification newNotification);

    Optional<List<Notification>> findByRecieverId(RecieverId recieverId);

    Optional<List<Notification>> findByRecieverIdFirst5(RecieverId recieverId);

    int getTotalNotificationFromRecieverId(RecieverId recieverId);
}
