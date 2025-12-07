package be.kdg.ip3.archportal.analytics.domain.records;

import java.util.UUID;

public record AchievementId(UUID id) {
    public static AchievementId create() {
        return new AchievementId(UUID.randomUUID());
    }
}
