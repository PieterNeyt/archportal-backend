package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameStatisticsRepository {
    Optional<GameStatistics> findById(GameId gameId);
    void save(GameStatistics restaurant);
    List<GameStatistics> findAll();
}
