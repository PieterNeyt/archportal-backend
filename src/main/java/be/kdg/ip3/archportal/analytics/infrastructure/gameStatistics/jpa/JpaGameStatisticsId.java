package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
public class JpaGameStatisticsId implements Serializable {
    private UUID gameId;
    private UUID profileId;

    protected JpaGameStatisticsId() {}

    public JpaGameStatisticsId(UUID gameId, UUID profileId) {
        this.gameId = gameId;
        this.profileId = profileId;
    }

    public static JpaGameStatisticsId fromDomain(GameStatisticsId gameStatisticsId) {
        return new JpaGameStatisticsId(
                gameStatisticsId.gameId().id(),
                gameStatisticsId.profileId().id()
        );
    }
}
