package be.kdg.ip3.archportal.analytics;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.*;
import be.kdg.ip3.archportal.analytics.domain.records.*;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAchievementsFromProfileSociableTest {
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
    class GetAchievementsFromProfileTests {
        @Test
        void getAchievementsFromProfile_validPlayer_returnsAllAchievements() {
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
            when(gamesApi.getAllAchievements(any(Map.class), any(List.class))).thenReturn(List.of(achievementDto));

            // Act
            var result = service.getAchievementsFromProfile(playerId);

            // Assert
            assertThat(result).isNotEmpty();
            assertThat(result).contains(achievementDto);
        }

        @Test
        void getAchievementsFromProfile_playerWithMultipleGames_returnsAllAchievements() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId1 = new GameId(UUID.randomUUID());
            var gameId2 = new GameId(UUID.randomUUID());
            var achievementId1 = UUID.randomUUID();
            var achievementId2 = UUID.randomUUID();
            var gameStatsId1 = new GameStatisticsId(gameId1, playerId);
            var gameStatsId2 = new GameStatisticsId(gameId2, playerId);

            var gameStats1 = new GameStatistics(gameStatsId1);
            gameStats1.addAchievement(new AchievementId(achievementId1));

            var gameStats2 = new GameStatistics(gameStatsId2);
            gameStats2.addAchievement(new AchievementId(achievementId2));

            var playerStats = new PlayerStatistics(playerId, List.of(gameStats1, gameStats2), java.time.Duration.ZERO, null);
            var achievementDto1 = new AchievementDto(achievementId1, "First Achievement", "Description 1", "image1.jpg", LocalDateTime.now());
            var achievementDto2 = new AchievementDto(achievementId2, "Second Achievement", "Description 2", "image2.jpg", LocalDateTime.now());

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.getAllAchievements(any(Map.class), any(List.class))).thenReturn(List.of(achievementDto1, achievementDto2));

            // Act
            var result = service.getAchievementsFromProfile(playerId);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).contains(achievementDto1, achievementDto2);
        }

        @Test
        void getAchievementsFromProfile_noAchievements_returnsEmptyList() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.getAllAchievements(any(Map.class), any(List.class))).thenReturn(List.of());

            // Act
            var result = service.getAchievementsFromProfile(playerId);

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void getAchievementsFromProfile_playerDoesNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());

            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.getAchievementsFromProfile(playerId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}

