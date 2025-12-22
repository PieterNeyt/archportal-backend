package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDateTime;

@ValueObject
@Getter
public class Achievement {
    private final AchievementId achievementId;
    private final LocalDateTime timeUnlocked;

    public Achievement(AchievementId achievementId, LocalDateTime timeUnlocked) {
        this.achievementId = achievementId;
        this.timeUnlocked = timeUnlocked;
    }

    public Achievement(AchievementId achievementId) {
        this(achievementId, LocalDateTime.now());
    }
}
