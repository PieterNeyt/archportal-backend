package be.kdg.ip3.archportal.analytics.infrastructure.messaging.config;

import java.util.UUID;

public record AchievementUnlockedMessage(
        String externalAchId,
        UUID playerId,
        UUID gameId
) {
}