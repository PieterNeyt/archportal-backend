package be.kdg.ip3.archportal.analytics.shared;

import org.springframework.modulith.NamedInterface;

import java.util.List;
import java.util.UUID;

@NamedInterface
public interface AnalyticsApi {
    void instantiateGameStatistics(List<CreateGameStatsDto> dto);
}
