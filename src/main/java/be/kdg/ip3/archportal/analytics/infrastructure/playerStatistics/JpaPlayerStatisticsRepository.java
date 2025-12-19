package be.kdg.ip3.archportal.analytics.infrastructure.playerStatistics;

import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsEntity;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsId;
import be.kdg.ip3.archportal.analytics.infrastructure.playerStatistics.jpa.JpaPlayerStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPlayerStatisticsRepository extends JpaRepository<JpaPlayerStatisticsEntity, UUID> {
}
