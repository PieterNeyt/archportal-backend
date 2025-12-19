package be.kdg.ip3.archportal.analytics.domain;


import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@AggregateRoot
@Getter
public class GameStatistics {
    private final GameStatisticsId gameStatisticsId;
    private Duration TotalPlayTimeMinutes;
    private LocalDateTime lastPlayedAt;
    private Set<Achievement> achievements;
    private List<WinnerRecord> winnerRecords;

    public GameStatistics(GameStatisticsId gameStatisticsId, Duration totalPlayTimeMinutes, LocalDateTime lastPlayedAt, List<Achievement> achievements, List<WinnerRecord> winnerRecords) {
        this.gameStatisticsId = gameStatisticsId;
        this.TotalPlayTimeMinutes = totalPlayTimeMinutes;
        this.lastPlayedAt = lastPlayedAt;
        this.achievements = new HashSet<>(achievements);
        this.winnerRecords = new ArrayList<>(winnerRecords);
    }

    public GameStatistics(GameStatisticsId gameStatisticsId) {
        this.gameStatisticsId = gameStatisticsId;
        this.TotalPlayTimeMinutes = Duration.ofMinutes(0);
        this.lastPlayedAt = LocalDateTime.of(0,  1, 1, 0, 0, 0 );
        this.achievements = new HashSet<>();
        this.winnerRecords = new ArrayList<>();
    }

    public void addWinnerRecord(WinnerRecord winnerRecord) {
        this.winnerRecords.add(winnerRecord);
    }

    public void addAchievement(AchievementId achievementId) {
        this.achievements.add(new Achievement(achievementId));
    }
}
