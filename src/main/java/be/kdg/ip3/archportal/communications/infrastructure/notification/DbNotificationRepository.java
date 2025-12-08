package be.kdg.ip3.archportal.communications.infrastructure.notification;

import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationId;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationRepository;
import be.kdg.ip3.archportal.communications.domain.notification.ReceiverId;
import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationEntity;
import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<List<Notification>> findByReceiverId(ReceiverId receiverId) {
        return this.repository.findByReceiverId(receiverId.id())
                .map(list -> list.stream()
                        .map(JpaNotificationEntity::toDomain)
                        .toList());
    }

    @Override
    public Optional<List<Notification>> findByReceiverIdFirstAmount(ReceiverId receiverId, int  amount) {
        return this.repository.findByReceiverId(receiverId.id(), Pageable.ofSize(amount))
                .map(list -> list.stream()
                        .map(JpaNotificationEntity::toDomain)
                        .toList());    }

    @Override
    public int getTotalNotificationFromReceiverId(ReceiverId receiverId) {
        return this.repository.countByReceiverId(receiverId.id());
    }

    @Override
    public Optional<Notification> findById(NotificationId notificationId) {
        return this.repository.findById(notificationId.id()).map(JpaNotificationEntity::toDomain);
    }

    @Override
    public void delete(Notification notification) {
        var jpaNotification = JpaNotificationEntity.fromDomain(notification);
        this.repository.delete(jpaNotification);
    }
}
