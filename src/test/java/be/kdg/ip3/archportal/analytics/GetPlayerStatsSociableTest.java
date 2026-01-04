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
public class GetPlayerStatsSociableTest {
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
    class GetPlayerStatsTests {
        @Test
        void getPlayerStats_validPlayer_returnsPlayerStatistics() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ofHours(5), null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            var result = service.getPlayerStats(playerId);

            // Assert
            assertThat(result).isEqualTo(playerStats);
            assertThat(result.getPlayerId()).isEqualTo(playerId);
            assertThat(result.getTotalTimePlayed()).isEqualTo(java.time.Duration.ofHours(5));
        }

        @Test
        void getPlayerStats_playerWithMultipleGames_returnsAllStats() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId1 = new GameId(UUID.randomUUID());
            var gameId2 = new GameId(UUID.randomUUID());
            var gameStatsId1 = new GameStatisticsId(gameId1, playerId);
            var gameStatsId2 = new GameStatisticsId(gameId2, playerId);

            var gameStats1 = new GameStatistics(gameStatsId1);
            var gameStats2 = new GameStatistics(gameStatsId2);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats1, gameStats2), java.time.Duration.ofHours(10), null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            var result = service.getPlayerStats(playerId);

            // Assert
            assertThat(result.getGameStatistics()).hasSize(2);
            assertThat(result.getGameStatistics()).contains(gameStats1, gameStats2);
        }

        @Test
        void getPlayerStats_newPlayer_returnsEmptyStats() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var playerStats = new PlayerStatistics(playerId);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act
            var result = service.getPlayerStats(playerId);

            // Assert
            assertThat(result).isEqualTo(playerStats);
            assertThat(result.getGameStatistics()).isEmpty();
            assertThat(result.getTotalTimePlayed()).isEqualTo(java.time.Duration.ZERO);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void getPlayerStats_playerDoesNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.getPlayerStats(playerId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}

