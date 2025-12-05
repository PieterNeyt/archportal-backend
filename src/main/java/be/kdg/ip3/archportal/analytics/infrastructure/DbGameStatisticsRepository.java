package be.kdg.ip3.archportal.analytics.infrastructure;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.GameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.JpaGameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsEntity;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsId;
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
    public Optional<GameStatistics> findById(GameStatisticsId id) {
        JpaGameStatisticsId jpaId = new JpaGameStatisticsId(
                id.gameId().id(),
                id.profileId().id()
        );

        return jpaGameStatisticsRepository
                .findById(jpaId)
                .map(JpaGameStatisticsEntity::toDomain);
    }


    @Override
    public void save(GameStatistics stats) {
        this.jpaGameStatisticsRepository.save(JpaGameStatisticsEntity.fromDomain(stats));
    }

    @Override
    public List<GameStatistics> findAll() {

        return jpaGameStatisticsRepository.findAll()
                .stream()
                .map(JpaGameStatisticsEntity::toDomain)
                .toList();
    }
}
