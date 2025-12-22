package be.kdg.ip3.archportal.analytics.infrastructure;

import be.kdg.ip3.archportal.analytics.domain.PlayerStatistics;
import be.kdg.ip3.archportal.analytics.domain.PlayerStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.infrastructure.playerStatistics.JpaPlayerStatisticsRepository;
import be.kdg.ip3.archportal.analytics.infrastructure.playerStatistics.jpa.JpaPlayerStatisticsEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DbPlayerStatisticsRepository implements PlayerStatisticsRepository {
    private final JpaPlayerStatisticsRepository jpaPlayerStatisticsRepository;


    public DbPlayerStatisticsRepository(JpaPlayerStatisticsRepository jpaPlayerStatisticsRepository) {
        this.jpaPlayerStatisticsRepository = jpaPlayerStatisticsRepository;
    }

    @Override
    public Optional<PlayerStatistics> findById(PlayerId playerId) {
        return this.jpaPlayerStatisticsRepository.findById(playerId.id()).map(JpaPlayerStatisticsEntity::toDomain);
    }

    @Override
    public void save(PlayerStatistics playerStats) {
        var jpaEntity = JpaPlayerStatisticsEntity.fromDomain(playerStats);
        this.jpaPlayerStatisticsRepository.save(jpaEntity);
    }
}
