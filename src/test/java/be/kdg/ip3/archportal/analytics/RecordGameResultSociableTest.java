package be.kdg.ip3.archportal.analytics;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.*;
import be.kdg.ip3.archportal.analytics.domain.records.*;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecordGameResultSociableTest {
    @Mock
    PlayerStatisticsRepository playerStatisticsRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    LobbiesApi lobbiesApi;
    @Mock
    GamesApi gamesApi;
    @Mock
    ProfilesApi profilesApi;

    AnalyticsService service;
    private static final int DEFAULT_PLATFORM_ACHIEVEMENT_POINTS = 100;

    @BeforeEach
    void setUp() {
        service = new AnalyticsService(playerStatisticsRepository, applicationEventPublisher, lobbiesApi, gamesApi, profilesApi, DEFAULT_PLATFORM_ACHIEVEMENT_POINTS);
    }

    @Nested
    class RecordGameResultTests {
        @Test
        void recordGameResult_validSessionAndPlayer_recordsWinnerAndSaves() {
            // Arrange
            var sessionId = new SessionId(UUID.randomUUID());
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var winner = "Player1";
            var timestamp = LocalDateTime.now();

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(lobbiesApi.getPlayerIdBySessionId(sessionId.id())).thenReturn(playerId.id());
            when(lobbiesApi.getGameIdBySessionId(sessionId.id())).thenReturn(gameId.id());
            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            service.recordGameResult(sessionId, winner, timestamp);

            // Assert
            verify(playerStatisticsRepository).save(playerStats);
        }

        @Test
        void recordGameResult_multipleGames_allRecorded() {
            // Arrange
            var sessionId1 = new SessionId(UUID.randomUUID());
            var sessionId2 = new SessionId(UUID.randomUUID());
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var timestamp = LocalDateTime.now();

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(lobbiesApi.getPlayerIdBySessionId(sessionId1.id())).thenReturn(playerId.id());
            when(lobbiesApi.getGameIdBySessionId(sessionId1.id())).thenReturn(gameId.id());
            when(lobbiesApi.getPlayerIdBySessionId(sessionId2.id())).thenReturn(playerId.id());
            when(lobbiesApi.getGameIdBySessionId(sessionId2.id())).thenReturn(gameId.id());
            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            service.recordGameResult(sessionId1, "Player1", timestamp);
            service.recordGameResult(sessionId2, "Player2", timestamp);

            // Assert
            verify(playerStatisticsRepository, times(2)).save(playerStats);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void recordGameResult_profileDoesNotExist_throwsNotFoundException() {
            // Arrange
            var sessionId = new SessionId(UUID.randomUUID());
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var winner = "Player1";
            var timestamp = LocalDateTime.now();

            when(lobbiesApi.getPlayerIdBySessionId(sessionId.id())).thenReturn(playerId.id());
            when(lobbiesApi.getGameIdBySessionId(sessionId.id())).thenReturn(gameId.id());
            when(profilesApi.existsById(playerId.id())).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> service.recordGameResult(sessionId, winner, timestamp))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void recordGameResult_playerStatsDoNotExist_throwsNotFoundException() {
            // Arrange
            var sessionId = new SessionId(UUID.randomUUID());
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var winner = "Player1";
            var timestamp = LocalDateTime.now();

            when(lobbiesApi.getPlayerIdBySessionId(sessionId.id())).thenReturn(playerId.id());
            when(lobbiesApi.getGameIdBySessionId(sessionId.id())).thenReturn(gameId.id());
            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.recordGameResult(sessionId, winner, timestamp))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}

