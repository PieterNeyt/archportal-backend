package be.kdg.ip3.archportal.analytics.api;

import be.kdg.ip3.archportal.analytics.api.dto.GameStatisticsDto;
import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.domain.records.ProfileId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/gameStats/{gameId}")
    public ResponseEntity<GameStatisticsDto> getGameStatistics(@AuthenticationPrincipal Jwt jwt, @PathVariable("gameId") UUID gameUUId) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));
        var gameId = new GameId(gameUUId);

        GameStatistics gameStatistics = analyticsService.getGameStatistics(playerId, gameId);
        return ResponseEntity.ok(GameStatisticsDto.fromDomain(gameStatistics));
    }


    @PostMapping("/achievement/{externalAchievementId}/user/{userId}")
    public ResponseEntity<Void> grantAchievement(@PathVariable("externalAchievementId") String externalAchId,@PathVariable("userId") UUID userUUId) {
        var userId = new ProfileId(userUUId);

        analyticsService.grantAchievement(externalAchId,userId);

        var location = URI.create("/api/analytics/achievement/" + externalAchId + "/user/" + userId);
        return ResponseEntity.created(location).build();
    }
}
