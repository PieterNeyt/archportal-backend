package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
public class JpaGameStatisticsId implements Serializable {
    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "player_statistics_id")
    private UUID playerStatisticsId;

    protected JpaGameStatisticsId() {}

    public JpaGameStatisticsId(UUID gameId, UUID playerStatisticsId) {
        this.gameId = gameId;
        this.playerStatisticsId = playerStatisticsId;
    }
}
