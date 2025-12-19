package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import be.kdg.ip3.archportal.analytics.domain.*;
import be.kdg.ip3.archportal.analytics.domain.records.*;
import be.kdg.ip3.archportal.analytics.infrastructure.playerStatistics.jpa.JpaPlayerStatisticsEntity;
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
    private UUID gameStatisticsId;

    @Column(name = "total_playtime_minutes")
    private long totalPlayTimeMinutes;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    @ElementCollection
    @CollectionTable(
            name = "game_statistics_achievements",
            schema = "analyticsservice",
            joinColumns = {
                    @JoinColumn(name = "game_id", referencedColumnName = "game_id"),
                    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
            }
    )
    private List<JpaAchievement> achievements;

    @ElementCollection
    @CollectionTable(
            name = "game_statistics_winner_records",
            schema = "analyticsservice",
            joinColumns = {
                    @JoinColumn(name = "game_id", referencedColumnName = "game_id"),
                    @JoinColumn(name = "profile_id", referencedColumnName = "profile_id")
            }
    )
    private List<JpaWinnerRecord> winnerRecords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_statistics_id")
    private JpaPlayerStatisticsEntity playerStatistics;

    protected JpaGameStatisticsEntity() { }

    public static JpaGameStatisticsEntity fromDomain(GameStatistics stats) {
        JpaGameStatisticsEntity entity = new JpaGameStatisticsEntity();

        entity.gameStatisticsId = stats.getGameStatisticsId().gameId();
        entity.totalPlayTimeMinutes = stats.getTotalPlayTimeMinutes().toMinutes();
        entity.lastPlayedAt = stats.getLastPlayedAt();

        entity.achievements = stats.getAchievements().stream()
                .map(a -> new JpaAchievement(
                        a.getAchievementId().id(),
                        a.getTimeUnlocked()
                ))
                .toList();

        entity.winnerRecords = stats.getWinnerRecords().stream()
                .map(w -> new JpaWinnerRecord(
                        w.PlayedAt(),
                        w.Winner(),
                        w.SessionId().id()
                ))
                .toList();

        return entity;
    }

    public GameStatistics toDomain() {
        return new GameStatistics(
                new GameStatisticsId(gameStatisticsId),
                Duration.ofMinutes(totalPlayTimeMinutes),
                lastPlayedAt,
                achievements.stream()
                        .map(a -> new Achievement(
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

    public void setPlayerStatistics(JpaPlayerStatisticsEntity playerStatistics) {
        this.playerStatistics = playerStatistics;
    }
}
