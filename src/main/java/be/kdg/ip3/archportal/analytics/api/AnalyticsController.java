package be.kdg.ip3.archportal.analytics.api;

import be.kdg.ip3.archportal.analytics.api.dto.GameStatisticsDto;
import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/gameStats/{profileId}/{gameId}")
    public ResponseEntity<GameStatisticsDto> getGameStatistics(@PathVariable UUID profileId, @PathVariable UUID gameId) {
        GameStatistics gameStatistics = analyticsService.getGameStatistics(profileId, gameId);
        return ResponseEntity.ok(GameStatisticsDto.fromDomain(gameStatistics));

    }


}
