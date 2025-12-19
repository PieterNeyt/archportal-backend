package be.kdg.ip3.archportal.analytics.infrastructure.playerStatistics.jpa;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.PlayerStatistics;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "player_statistics", schema = "analyticsservice")
public class JpaPlayerStatisticsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PlayerId playerId;

    @OneToMany(
            mappedBy = "playerStatistics",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<JpaGameStatisticsEntity> gameStatistics = new ArrayList<>();

    @Column(name = "total_minutes_played", nullable = false)
    private int totalMinutesPlayed;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_played")
    private Date lastPlayed;

    public JpaPlayerStatisticsEntity(PlayerId playerId, int totalMinutesPlayed, Date lastPlayed) {
        this.playerId = playerId;
        this.totalMinutesPlayed = totalMinutesPlayed;
        this.lastPlayed = lastPlayed;
    }

    public JpaPlayerStatisticsEntity() {}

    public static JpaPlayerStatisticsEntity fromDomain(PlayerStatistics stats) {
        JpaPlayerStatisticsEntity entity = new JpaPlayerStatisticsEntity(
                stats.getPlayerId(),
                stats.getTotalMinutesPlayed(),
                stats.getLastPlayed()
        );

        entity.gameStatistics = stats.getGameStatistics().stream()
                .map(gs -> {
                    var gameEntity = JpaGameStatisticsEntity.fromDomain(gs);
                    gameEntity.setPlayerStatistics(entity);
                    return gameEntity;
                })
                .toList();

        return entity;
    }

    public PlayerStatistics toDomain() {
        List<GameStatistics> games = this.gameStatistics.stream()
                .map(JpaGameStatisticsEntity::toDomain)
                .toList();

        return new PlayerStatistics(
                this.playerId,
                games,
                this.totalMinutesPlayed,
                this.lastPlayed
        );
    }
}
