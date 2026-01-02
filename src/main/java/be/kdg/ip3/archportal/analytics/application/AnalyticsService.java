package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.domain.*;
import be.kdg.ip3.archportal.analytics.domain.records.*;
import be.kdg.ip3.archportal.analytics.shared.AnalyticsApi;
import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.games.shared.AchievementDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformPointsEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AnalyticsService implements AnalyticsApi {
    private final PlayerStatisticsRepository playerStatisticsRepository;
    private final ApplicationEventPublisher publisher;
    private final LobbiesApi lobbiesApi;
    private final GamesApi gamesApi;
    private final ProfilesApi profilesApi;
    private final int defaultPlatformAchievementPoints;

    public AnalyticsService(PlayerStatisticsRepository playerStatisticsRepository, ApplicationEventPublisher publisher, LobbiesApi lobbiesApi, GamesApi gamesApi, ProfilesApi profilesApi,@Value("${achievement.defaultplatformpoints}") int defaultPlatformAchievementPoints) {
        this.playerStatisticsRepository = playerStatisticsRepository;
        this.publisher = publisher;
        this.lobbiesApi = lobbiesApi;
        this.gamesApi = gamesApi;
        this.profilesApi = profilesApi;
        this.defaultPlatformAchievementPoints = defaultPlatformAchievementPoints;
    }

    public void recordGameResult(SessionId sessionId, String winner, LocalDateTime timestamp) {
        var proUuid = lobbiesApi.getPlayerIdBySessionId(sessionId.id());
        var gameUUid = lobbiesApi.getGameIdBySessionId(sessionId.id());

        var playerId = new PlayerId(proUuid);
        var gameId = new GameId(gameUUid);
        var gameStatsId = new GameStatisticsId(gameId,playerId);

        if (!profilesApi.existsById(playerId.id()))
            throw new NotFoundException("Profile not found");


        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Player not found"));

        var result = new WinnerRecord(
                timestamp, winner, sessionId
        );
        playerStats.addWinnerRecord(result, gameStatsId);
        playerStatisticsRepository.save(playerStats);
    }


    @Override
    public void instantiateGameStatistics(List<CreateGameStatsDto> dto) {
        dto.forEach(this::instantiateSingleGameStatistics);
    }

    private void instantiateSingleGameStatistics(CreateGameStatsDto dto) {
        var playerId = new PlayerId(dto.profileID());
        var gameId = new GameId(dto.gameID());
        var gameStatsId = new GameStatisticsId(gameId,playerId);

        if (!gamesApi.validateGame(gameId.id()))
            throw new NotFoundException("Game not found");

        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        playerStats.addGameStatistics(gameStatsId);

        playerStatisticsRepository.save(playerStats);
    }

    public GameStatistics getGameStatistics(PlayerId playerId, GameId gameId) {
        var gameStatsId = new GameStatisticsId(gameId,playerId);

        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        return playerStats.findGameStatisticsById(gameStatsId);
    }

    public void grantAchievement(String externalAchId, PlayerId playerId, GameStatisticsId gameStatsId) {
        if (!profilesApi.existsById(playerId.id()))
            throw new NotFoundException("Profile not found");

        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        var achievement = gamesApi.findAchievementByExternalAchId(externalAchId,gameStatsId.gameId().id());

        playerStats.addAchievementToGame(new AchievementId(achievement.achievementId()),gameStatsId);
        playerStatisticsRepository.save(playerStats);

        publisher.publishEvent(new AddNotificationEvent(
                playerId.id(),
                "New achievement unlocked!",
                "You unlocked \"" + achievement.title() + "\". Well done!",
                NotificationType.ACHIEVEMENT
        ));
        publisher.publishEvent(new GrantPlatformPointsEvent(
                playerId.id(),
                defaultPlatformAchievementPoints
        ));
    }

    public List<AchievementDto> getAchievements(PlayerId playerId, GameStatisticsId gameStatsId) {
        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        var achievementIds = playerStats.getAchievementIds(gameStatsId);

        return gamesApi.getAchievements(achievementIds,gameStatsId.gameId().id());
    }

    public void updateStatistics(GameStatsCommand command) {
        var playerStats = playerStatisticsRepository.findById(command.playerId())
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        var gameStatsId = new GameStatisticsId(command.gameId(),command.playerId());
        playerStats.update(gameStatsId,command.startTime(),command.endTime());

        playerStatisticsRepository.save(playerStats);
    }

    public PlayerStatistics getPlayerStats(PlayerId playerId) {
        return playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));
    }

    public  List<AchievementDto>  getAchievementsFromProfile(PlayerId playerId) {
        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        var achievementIds = playerStats.getAllAchievementIds();
        var gameIds = playerStats.getAllGameIds();
        return gamesApi.getAllAchievements(achievementIds,gameIds);
    }
}
