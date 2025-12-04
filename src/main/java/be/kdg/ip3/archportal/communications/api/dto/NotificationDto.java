package be.kdg.ip3.archportal.communications.api.dto;

import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.shared.NotificationType;

import java.util.Date;
import java.util.UUID;

public record NotificationDto(UUID id,
                              String title,
                              String body,
                              NotificationType notificationType,
                              Date createdAt) {
    public static NotificationDto fromDomain(Notification notification) {
        return new NotificationDto(notification.getId().id(),
                notification.getTitle(),
                notification.getBody(),
                notification.getType(),
                notification.getCreatedAt());
    }
}
