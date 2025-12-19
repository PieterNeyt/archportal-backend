package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@Getter
public class JpaAchievement {

    @Column(nullable = false)
    private UUID achievementId;

    @Column(nullable = false)
    private LocalDateTime timeUnlocked;

    protected JpaAchievement() { }

    public JpaAchievement(UUID achievementId, LocalDateTime timeUnlocked) {
        this.achievementId = achievementId;
        this.timeUnlocked = timeUnlocked;
    }
}
