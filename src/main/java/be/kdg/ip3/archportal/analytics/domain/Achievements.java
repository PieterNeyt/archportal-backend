package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDateTime;

@ValueObject
@Getter
public class Achievements {
    private final AchievementId achievementId;
    private LocalDateTime timeUnlocked;

    public Achievements(AchievementId achievementId, LocalDateTime timeUnlocked) {
        this.achievementId = achievementId;
        this.timeUnlocked = timeUnlocked;
    }

    //TODO: verder uitwerken, dit is skelly
}
