package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics;

import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsEntity;
import be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa.JpaGameStatisticsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGameStatisticsRepository extends JpaRepository<JpaGameStatisticsEntity, JpaGameStatisticsId> {
}
