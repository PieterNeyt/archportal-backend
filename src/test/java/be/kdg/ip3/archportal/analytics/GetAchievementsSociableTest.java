package be.kdg.ip3.archportal.analytics;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.NotFoundException;
import be.kdg.ip3.archportal.analytics.domain.PlayerStatistics;
import be.kdg.ip3.archportal.analytics.domain.PlayerStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.games.shared.AchievementDto;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAchievementsSociableTest {
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
    class GetAchievementsTests {
        @Test
        void getAchievements_validGameStats_returnsAchievementList() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var achievementId = UUID.randomUUID();
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            gameStats.addAchievement(new AchievementId(achievementId));

            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);
            var achievementDto = new AchievementDto(achievementId, "Achievement Title", "Description", "image.jpg", LocalDateTime.now());

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.getAchievements(any(Map.class), eq(gameId.id()))).thenReturn(List.of(achievementDto));

            // Act
            var result = service.getAchievements(playerId, gameStatsId);

            // Assert
            assertThat(result).isNotEmpty();
            assertThat(result).contains(achievementDto);
        }

        @Test
        void getAchievements_noAchievementsForGame_returnsEmptyList() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.getAchievements(any(Map.class), eq(gameId.id()))).thenReturn(List.of());

            // Act
            var result = service.getAchievements(playerId, gameStatsId);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        void getAchievements_multipleAchievements_returnsAllAchievements() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var achievementId1 = UUID.randomUUID();
            var achievementId2 = UUID.randomUUID();
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            gameStats.addAchievement(new AchievementId(achievementId1));
            gameStats.addAchievement(new AchievementId(achievementId2));

            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);
            var achievementDto1 = new AchievementDto(achievementId1, "First Achievement", "Description 1", "image1.jpg", LocalDateTime.now());
            var achievementDto2 = new AchievementDto(achievementId2, "Second Achievement", "Description 2", "image2.jpg", LocalDateTime.now());

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.getAchievements(any(Map.class), eq(gameId.id()))).thenReturn(List.of(achievementDto1, achievementDto2));

            // Act
            var result = service.getAchievements(playerId, gameStatsId);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).contains(achievementDto1, achievementDto2);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void getAchievements_playerStatsDoNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.getAchievements(playerId, gameStatsId))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void getAchievements_gameStatisticsNotFound_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var nonExistentGameStatsId = new GameStatisticsId(new GameId(UUID.randomUUID()), playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));

            // Act & Assert
            assertThatThrownBy(() -> service.getAchievements(playerId, nonExistentGameStatsId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}

