package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerStatisticsRepository {

    Optional<PlayerStatistics> findById(PlayerId playerId);

    void save(PlayerStatistics playerStats);
}
