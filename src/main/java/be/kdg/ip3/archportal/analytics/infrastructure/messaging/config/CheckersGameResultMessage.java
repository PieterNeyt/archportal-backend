package be.kdg.ip3.archportal.analytics.infrastructure.messaging.config;

import java.time.LocalDateTime;
import java.util.UUID;

public record CheckersGameResultMessage(UUID sessionId, String winner, String timestamp) {
}
