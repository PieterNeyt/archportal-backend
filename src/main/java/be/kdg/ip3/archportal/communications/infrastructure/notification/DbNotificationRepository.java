package be.kdg.ip3.archportal.communications.infrastructure.notification;

import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationId;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationRepository;
import be.kdg.ip3.archportal.communications.domain.notification.RecieverId;
import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationEntity;
import be.kdg.ip3.archportal.communications.infrastructure.notification.jpa.JpaNotificationRepository;
import org.hibernate.query.spi.Limit;
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
    public Optional<List<Notification>> findByRecieverId(RecieverId recieverId) {
        return this.repository.findByRecieverId(recieverId.id())
                .map(list -> list.stream()
                        .map(JpaNotificationEntity::toDomain)
                        .toList());
    }

    @Override
    public Optional<List<Notification>> findByRecieverIdFirst5(RecieverId recieverId) {
        return this.repository.findByRecieverId(recieverId.id(), Pageable.ofSize(5))
                .map(list -> list.stream()
                        .map(JpaNotificationEntity::toDomain)
                        .toList());    }

    @Override
    public int getTotalNotificationFromRecieverId(RecieverId recieverId) {
        return this.repository.countByRecieverId(recieverId.id());
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
