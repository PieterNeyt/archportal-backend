package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.domain.*;
import be.kdg.ip3.archportal.analytics.domain.records.*;
import be.kdg.ip3.archportal.analytics.shared.AnalyticsApi;
import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AnalyticsService implements AnalyticsApi {
    private final GameStatisticsRepository gameStatisticsRepository;
    private final PlayerStatisticsRepository playerStatisticsRepository;
    private final LobbiesApi lobbiesApi;
    private final GamesApi gamesApi;
    private final ProfilesApi profilesApi;

    public AnalyticsService(GameStatisticsRepository gameStatisticsRepository, PlayerStatisticsRepository playerStatisticsRepository, LobbiesApi lobbiesApi, GamesApi gamesApi, ProfilesApi profilesApi) {
        this.gameStatisticsRepository = gameStatisticsRepository;
        this.playerStatisticsRepository = playerStatisticsRepository;
        this.lobbiesApi = lobbiesApi;
        this.gamesApi = gamesApi;
        this.profilesApi = profilesApi;
    }

    public void recordGameResult(SessionId sessionId,  String winner, LocalDateTime timestamp) {
        var proUuid =  lobbiesApi.getPlayerIdBySessionId(sessionId.id());
        var gameUUid = lobbiesApi.getGameIdBySessionId(sessionId.id());

        var playerId = new PlayerId(proUuid);
        var gameId = new GameStatisticsId(gameUUid);

        if(!profilesApi.existsById(playerId.id()))
            throw new NotFoundException("Profile not found");


        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Player not found"));

        var result = new WinnerRecord(
                timestamp, winner, sessionId
        );
        playerStats.addWinnerRecord(result,gameId);
        playerStatisticsRepository.save(playerStats);
    }


    @Override
    public void instantiateGameStatistics(List<CreateGameStatsDto> dto) {
        dto.forEach(this::instantiateSingleGameStatistics);
    }

    private void instantiateSingleGameStatistics(CreateGameStatsDto dto) {
        var gameId = new GameStatisticsId(dto.gameID());
        var playerId = new PlayerId(dto.profileID());

        if(gamesApi.validateGame(gameId.gameId()))
            throw new NotFoundException("Game not found");

        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        playerStats.addGameStatistics(gameId);

        playerStatisticsRepository.save(playerStats);
    }

    public GameStatistics getGameStatistics(PlayerId playerId, GameId gameId) {
        var gameStatisticsId = new GameStatisticsId(gameId.id());
        var playerStats = playerStatisticsRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("player stats not found"));

        return playerStats.findGameStatisticsById(gameStatisticsId);
    }

    public void grantAchievement(String externalAchId, ProfileId userId) {
        profilesApi.existsById(userId.id());

        var analyticsProfile =
        var achievementId = ;
    }
}
