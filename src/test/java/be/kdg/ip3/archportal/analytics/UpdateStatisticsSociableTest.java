package be.kdg.ip3.archportal.analytics;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.application.GameStatsCommand;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateStatisticsSociableTest {
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
    class UpdateStatisticsTests {
        @Test
        void updateStatistics_validCommand_updatesPlayerStatsAndSaves() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var startTime = LocalDateTime.now().minusHours(2);
            var endTime = LocalDateTime.now();

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);
            var command = new GameStatsCommand(gameId, playerId, startTime, endTime);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            service.updateStatistics(command);

            // Assert
            verify(playerStatisticsRepository).save(playerStats);
            assertThat(playerStats.getTotalTimePlayed()).isGreaterThan(java.time.Duration.ZERO);
            assertThat(playerStats.getLastPlayed()).isNotNull();
        }

        @Test
        void updateStatistics_multipleSessions_accumulatesPlayTime() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            var startTime1 = LocalDateTime.now().minusHours(4);
            var endTime1 = LocalDateTime.now().minusHours(2);
            var command1 = new GameStatsCommand(gameId, playerId, startTime1, endTime1);

            var startTime2 = LocalDateTime.now().minusHours(1);
            var endTime2 = LocalDateTime.now();
            var command2 = new GameStatsCommand(gameId, playerId, startTime2, endTime2);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            service.updateStatistics(command1);
            service.updateStatistics(command2);

            // Assert
            verify(playerStatisticsRepository, times(2)).save(playerStats);
            assertThat(playerStats.getTotalTimePlayed().toMinutes()).isGreaterThanOrEqualTo(120);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void updateStatistics_playerStatsDoNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var startTime = LocalDateTime.now().minusHours(1);
            var endTime = LocalDateTime.now();
            var command = new GameStatsCommand(gameId, playerId, startTime, endTime);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.updateStatistics(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}

