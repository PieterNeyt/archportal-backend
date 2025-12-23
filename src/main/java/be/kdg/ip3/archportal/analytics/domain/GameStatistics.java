package be.kdg.ip3.archportal.analytics.domain;


import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@AggregateRoot
@Getter
public class GameStatistics {
    private final GameStatisticsId gameStatisticsId;
    private Duration totalTimePlayed;
    private LocalDateTime lastPlayedAt;
    private Set<Achievement> achievements;
    private List<WinnerRecord> winnerRecords;

    public GameStatistics(GameStatisticsId gameStatisticsId, Duration totalPlayTimeMinutes, LocalDateTime lastPlayedAt, List<Achievement> achievements, List<WinnerRecord> winnerRecords) {
        this.gameStatisticsId = gameStatisticsId;
        this.totalTimePlayed = totalPlayTimeMinutes;
        this.lastPlayedAt = lastPlayedAt;
        this.achievements = new HashSet<>(achievements);
        this.winnerRecords = new ArrayList<>(winnerRecords);
    }

    public GameStatistics(GameStatisticsId gameStatisticsId) {
        this.gameStatisticsId = gameStatisticsId;
        this.totalTimePlayed = Duration.ZERO;
        this.lastPlayedAt = LocalDateTime.of(0,  1, 1, 0, 0, 0 );
        this.achievements = new HashSet<>();
        this.winnerRecords = new ArrayList<>();
    }

    public void addWinnerRecord(WinnerRecord winnerRecord) {
        this.winnerRecords.add(winnerRecord);
    }

    public void addAchievement(AchievementId achievementId) {
        boolean alreadyUnlocked = this.achievements.stream()
                .anyMatch(a -> a.getAchievementId().equals(achievementId));

        if (alreadyUnlocked)
            throw new IllegalStateException("Achievement '" + achievementId.id() + "' has already been unlocked for this player.");


        this.achievements.add(new Achievement(achievementId));
    }

    public Map<UUID, LocalDateTime> getAchievementIds() {
        return achievements.stream()
                .collect(Collectors.toMap(
                        achievement -> achievement.getAchievementId().id(),
                        Achievement::getTimeUnlocked,
                        (existing, replacement) -> existing
                ));
    }

    public void update(Duration timePlayed) {
        this.lastPlayedAt = LocalDateTime.now();
        this.totalTimePlayed = totalTimePlayed.plus(timePlayed);
    }
}
