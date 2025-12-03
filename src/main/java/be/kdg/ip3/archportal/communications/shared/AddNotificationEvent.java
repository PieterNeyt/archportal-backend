package be.kdg.ip3.archportal.communications.shared;

import java.util.UUID;

public record AddNotificationEvent(
        UUID recieverId,
        String title,
        String body,
        NotificationType type
        ) {
}
