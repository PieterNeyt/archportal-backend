package be.kdg.ip3.archportal.analytics.shared;

import java.util.UUID;

public record CreateGameStatsDto(UUID gameID, UUID profileID) {
}
