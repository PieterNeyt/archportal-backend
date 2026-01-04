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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetGameStatisticsSociableTest {
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
    class GetGameStatisticsTests {
        @Test
        void getGameStatistics_validPlayerAndGame_returnsGameStatistics() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            var result = service.getGameStatistics(playerId, gameId);

            // Assert
            assertThat(result).isEqualTo(gameStats);
            assertThat(result.getGameStatisticsId()).isEqualTo(gameStatsId);
        }

        @Test
        void getGameStatistics_multipleGames_returnsCorrectGameStatistics() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId1 = new GameId(UUID.randomUUID());
            var gameId2 = new GameId(UUID.randomUUID());
            var gameStatsId1 = new GameStatisticsId(gameId1, playerId);
            var gameStatsId2 = new GameStatisticsId(gameId2, playerId);

            var gameStats1 = new GameStatistics(gameStatsId1);
            var gameStats2 = new GameStatistics(gameStatsId2);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats1, gameStats2), java.time.Duration.ZERO, null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            var result = service.getGameStatistics(playerId, gameId2);

            // Assert
            assertThat(result).isEqualTo(gameStats2);
            assertThat(result).isNotEqualTo(gameStats1);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void getGameStatistics_playerDoesNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.getGameStatistics(playerId, gameId))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void getGameStatistics_gameStatisticsDoNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var otherGameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act & Assert
            assertThatThrownBy(() -> service.getGameStatistics(playerId, otherGameId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}

