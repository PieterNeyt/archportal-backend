package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.GameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.*;
import be.kdg.ip3.archportal.analytics.shared.AnalyticsApi;
import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class AnalyticsService implements AnalyticsApi {
    private final GameStatisticsRepository gameStatisticsRepository;
    private final LobbiesApi lobbiesApi;

    public AnalyticsService(GameStatisticsRepository gameStatisticsRepository, LobbiesApi lobbiesApi) {
        this.gameStatisticsRepository = gameStatisticsRepository;
        this.lobbiesApi = lobbiesApi;
    }

    public void recordGameResult(SessionId sessionId,  String winner, LocalDateTime timestamp) {
        var proUuid =  lobbiesApi.getPlayerIdBySessionId(sessionId.id());
        var gameUUid = lobbiesApi.getGameIdBySessionId(sessionId.id());

        var profileId = new ProfileId(proUuid);
        var gameId = new GameId(gameUUid);
        GameStatistics gameStats = gameStatisticsRepository.findById(new GameStatisticsId(gameId, profileId))
                .orElseThrow(() -> new IllegalArgumentException("No gameStatistic found for id: " + new GameStatisticsId(gameId, profileId)));
        ;

        var result = new WinnerRecord(
                timestamp, winner, sessionId
        );

        gameStats.addWinnerRecord(result);
        gameStatisticsRepository.save(gameStats);
    }


    @Override
    public void instantiateGameStatistics(CreateGameStatsDto dto) {
        GameId gameId = new GameId(dto.gID());
        ProfileId profileId = new ProfileId(dto.proID());

        GameStatistics gameStatistics = new GameStatistics(new GameStatisticsId(gameId, profileId));
        this.gameStatisticsRepository.save(gameStatistics);
        log.info( "Game statistics instantiated for game id: {} and profile id: {}", dto.gID(), dto.proID());
    }
    public GameStatistics getGameStatistics(UUID profileId, UUID gameId) {
        ProfileId pId = new ProfileId(profileId);
        GameId gId = new GameId(gameId);
        GameStatisticsId gameStatisticsId = new GameStatisticsId(gId, pId);

        return gameStatisticsRepository.findById(gameStatisticsId)
                .orElseThrow(() -> new IllegalArgumentException("No game statistics found for profile id: " + profileId + " and game id: " + gameId));
    }
}
