package be.kdg.ip3.archportal.games.domain.achievement;

import org.springframework.util.Assert;

import java.util.UUID;

public record AchievementId(UUID id) {

    public AchievementId {
        Assert.notNull(id, "id is null");
    }

    public static AchievementId create() {
        return new AchievementId(UUID.randomUUID());
    }
}
