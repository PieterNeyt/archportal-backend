package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import be.kdg.ip3.archportal.analytics.domain.Achievements;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.records.*;
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
    @EmbeddedId
    @Column(name = "game_id")
    private JpaGameStatisticsId gameStatisticsId;

    @Column(name = "total_playtime_minutes")
    private long totalPlayTimeMinutes;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    @ElementCollection
    @CollectionTable(
            name = "game_statistics_achievements",
            schema = "analyticsservice",
            joinColumns = {
                    @JoinColumn(name = "game_id", referencedColumnName = "gameId"),
                    @JoinColumn(name = "profile_id", referencedColumnName = "profileId")
            }
    )
    private List<JpaAchievement> achievements;


    @ElementCollection
    @CollectionTable(
            name = "game_statistics_winner_records",
            schema = "analyticsservice",
            joinColumns = {
                    @JoinColumn(name = "game_id", referencedColumnName = "gameId"),
                    @JoinColumn(name = "profile_id", referencedColumnName = "profileId")
            }
    )
    private List<JpaWinnerRecord> winnerRecords;
    protected JpaGameStatisticsEntity() { }


    public static JpaGameStatisticsEntity fromDomain(GameStatistics stats) {
        JpaGameStatisticsEntity entity = new JpaGameStatisticsEntity();


        entity.gameStatisticsId = JpaGameStatisticsId.fromDomain(stats.getGameStatisticsId());
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

    public GameStatistics toDomain() {
        return new GameStatistics(

                new GameStatisticsId(
                        new GameId(gameStatisticsId.getGameId()),
                        new ProfileId(gameStatisticsId.getProfileId())
                ),
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
