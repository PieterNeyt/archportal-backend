package be.kdg.ip3.archportal.lobbies.shared;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionEndedEvent(UUID gameId, UUID playerId, LocalDateTime startTime, LocalDateTime endTime) {
}
