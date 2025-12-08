package be.kdg.ip3.archportal.communications.shared;

import java.util.UUID;

public record AddNotificationEvent(
        UUID receiverId,
        String title,
        String body,
        NotificationType type
        ) {
}
