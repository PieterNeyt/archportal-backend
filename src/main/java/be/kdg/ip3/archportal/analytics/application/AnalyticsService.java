package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.GameStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.ProfileId;
import be.kdg.ip3.archportal.analytics.domain.records.SessionId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import be.kdg.ip3.archportal.analytics.shared.AnalyticsApi;
import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AnalyticsService implements AnalyticsApi {
    private final GameStatisticsRepository gameStatisticsRepository;

    public AnalyticsService(GameStatisticsRepository gameStatisticsRepository) {
        this.gameStatisticsRepository = gameStatisticsRepository;
    }

    public void recordGameResult(SessionId sessionId, GameId gameId, String winner, LocalDateTime timestamp) {
        GameStatistics game = gameStatisticsRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("No game found for id: " + gameId));
        ;

        var result = new WinnerRecord(
                timestamp, winner, sessionId
        );

        game.addWinnerRecord(result);
        gameStatisticsRepository.save(game);
    }


    @Override
    public void instantiateGameStatistics(CreateGameStatsDto dto) {
        GameId gameId = new GameId(dto.gID());
        ProfileId profileId = new ProfileId(dto.proID());

        GameStatistics gameStatistics = new GameStatistics(gameId, profileId);
        this.gameStatisticsRepository.save(gameStatistics);


    }
    public GameStatistics getGameStatistics(UUID profileId, UUID gameId) {
        ProfileId pId = new ProfileId(profileId);
        GameId gId = new GameId(gameId);

        return gameStatisticsRepository.findByProfileIdAndGameId(pId, gId)
                .orElseThrow(() -> new IllegalArgumentException("No game statistics found for profile id: " + profileId + " and game id: " + gameId));
    }
}
