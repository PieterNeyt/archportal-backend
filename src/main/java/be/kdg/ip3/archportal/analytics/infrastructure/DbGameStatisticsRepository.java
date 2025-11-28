package be.kdg.ip3.archportal.analytics.infrastructure;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.GameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.JpaGameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DbGameStatisticsRepository implements GameStatisticsRepository {
    private final JpaGameStatisticsRepository jpaGameStatisticsRepository;

    public DbGameStatisticsRepository(JpaGameStatisticsRepository jpaGameStatisticsRepository) {
        this.jpaGameStatisticsRepository = jpaGameStatisticsRepository;
    }

    @Override
    public Optional<GameStatistics> findById(GameId gameId) {
        return jpaGameStatisticsRepository.findById(gameId.id()).map(JpaGameStatisticsEntity::toDomain);
    }

    @Override
    public void save(GameStatistics restaurant) {

    }

    @Override
    public List<GameStatistics> findAll() {
        return List.of();
    }
}
