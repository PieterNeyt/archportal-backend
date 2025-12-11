package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.GameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.*;
import be.kdg.ip3.archportal.analytics.shared.AnalyticsApi;
import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public void instantiateGameStatistics(List<CreateGameStatsDto> dto) {
        dto.forEach(this::instantiateSingleGameStatistics);
    }

    private void instantiateSingleGameStatistics(CreateGameStatsDto dto) {
        var gameId = new GameId(dto.gameID());
        var profileId = new ProfileId(dto.profileID());

        var gameStatistics = new GameStatistics(new GameStatisticsId(gameId, profileId));
        this.gameStatisticsRepository.save(gameStatistics);
    }

    public GameStatistics getGameStatistics(ProfileId profileId, GameId gameId) {
        var gameStatisticsId = new GameStatisticsId(gameId, profileId);

        return gameStatisticsRepository.findById(gameStatisticsId)
                .orElseThrow(() -> new IllegalArgumentException("No game statistics found for profile id: " + profileId + " and game id: " + gameId));
    }
}
