package be.kdg.ip3.archportal.analytics.api;

import be.kdg.ip3.archportal.analytics.api.dto.GameStatisticsDto;
import be.kdg.ip3.archportal.analytics.api.dto.PlayerStatisticsDto;
import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.Achievement;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.domain.records.ProfileId;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.shared.AchievementDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
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
    @GetMapping("/{profileId}")
    public ResponseEntity<PlayerStatisticsDto> getAchievementsFromProfile(@PathVariable("profileId") UUID profileUUId) {
        var playerId = new PlayerId(profileUUId);

        var playerStats = analyticsService.getPlayerStats(playerId);
        return ResponseEntity.ok(PlayerStatisticsDto.fromDomain(
                playerStats.getTotalTimePlayed().toMinutes()
                ,playerStats.getLastPlayed()));
    }

    @GetMapping("/{profileId}/achievements")
    public ResponseEntity<List<AchievementDto>> getPlayerStats(@PathVariable("profileId") UUID profileUUId) {
        var playerId = new PlayerId(profileUUId);

        var achievements = analyticsService.getAchievementsFromProfile(playerId);
        return ResponseEntity.ok(achievements);
    }


    @PostMapping("/game/{gameId}/achievement/{externalAchievementId}/user/{userId}")
    public ResponseEntity<Void> grantAchievement(@PathVariable("externalAchievementId") String externalAchId,
                                                 @PathVariable("userId") UUID userUUId,
                                                     @PathVariable("gameId") UUID gameUUID) {
        var playerId = new PlayerId(userUUId);
        var gameId = new GameId(gameUUID);
        var gameStatsId = new GameStatisticsId(gameId,playerId);

        analyticsService.grantAchievement(externalAchId,playerId,gameStatsId);

        var location = UriComponentsBuilder.fromPath("/api/analytics/game/{gameId}/achievement/{achievementId}/user/{userId}")
                .buildAndExpand(gameUUID, externalAchId, playerId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/game/{gameId}/achievements")
    public ResponseEntity<List<AchievementDto>> getAchievements(@PathVariable("gameId") UUID gameUUID, @AuthenticationPrincipal Jwt jwt) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));
        var gameId = new GameId(gameUUID);
        var gameStatsId = new GameStatisticsId(gameId,playerId);

        var achievements = analyticsService.getAchievements(playerId,gameStatsId);

        return ResponseEntity.ok(achievements);
    }
}
