package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import be.kdg.ip3.archportal.analytics.domain.Achievements;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.SessionId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "game_statistics", schema = "analyticsservice")
public class JpaGameStatisticsEntity {

    @Id
    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "total_playtime_minutes")
    private long totalPlayTimeMinutes;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    @ElementCollection
    @CollectionTable(
            name = "game_statistics_achievements",
            schema = "analyticsservice",
            joinColumns = @JoinColumn(name = "game_id")
    )
    private List<JpaAchievement> achievements;


    @ElementCollection
    @CollectionTable(
            name = "game_statistics_winner_records",
            schema = "analyticsservice",
            joinColumns = @JoinColumn(name = "game_id")
    )
    private List<JpaWinnerRecord> winnerRecords;

    protected JpaGameStatisticsEntity() { }

    // ----------------------------
    //      FROM DOMAIN
    // ----------------------------
    public static JpaGameStatisticsEntity fromDomain(GameStatistics stats) {
        JpaGameStatisticsEntity entity = new JpaGameStatisticsEntity();

        entity.gameId = stats.getGameId().id();
        entity.totalPlayTimeMinutes = stats.getTotalPlayTimeMinutes().toMinutes();
        entity.lastPlayedAt = stats.getLastPlayedAt();
        entity.achievements = stats.getAchievements().stream()
                .map(a -> new JpaAchievement(
                        a.getAchievementId().id(),
                        a.getTimeUnlocked()
                ))
                .toList();

        entity.winnerRecords = stats.getWinnerRecords()
                .stream()
                .map(w -> new JpaWinnerRecord(
                        w.PlayedAt(),
                        w.Winner(),
                        w.SessionId().id()
                ))
                .toList();

        return entity;
    }

    // ----------------------------
    //      TO DOMAIN
    // ----------------------------
    public GameStatistics toDomain() {
        return new GameStatistics(
                new GameId(gameId),
                Duration.ofMinutes(totalPlayTimeMinutes),
                lastPlayedAt,
                achievements.stream()
                        .map(a -> new Achievements(
                                new AchievementId(a.getAchievementId()),
                                a.getTimeUnlocked()
                        ))
                        .toList(),
                winnerRecords.stream()
                        .map(w -> new WinnerRecord(
                                w.getPlayedAt(),
                                w.getWinner(),
                                new SessionId(w.getSessionId())
                        ))
                        .toList()
        );
    }
}
