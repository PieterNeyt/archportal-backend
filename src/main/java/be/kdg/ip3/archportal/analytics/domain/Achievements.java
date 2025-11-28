package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.time.LocalDateTime;

@Entity
@Getter
public class Achievements {
    private final AchievementId achievementId;
    private LocalDateTime timeUnlocked;

    public Achievements(AchievementId achievementId, LocalDateTime timeUnlocked) {
        this.achievementId = achievementId;
        this.timeUnlocked = timeUnlocked;
    }
}
