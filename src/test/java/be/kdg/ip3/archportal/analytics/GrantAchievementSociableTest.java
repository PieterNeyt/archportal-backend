package be.kdg.ip3.archportal.analytics;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.GameStatistics;
import be.kdg.ip3.archportal.analytics.domain.NotFoundException;
import be.kdg.ip3.archportal.analytics.domain.PlayerStatistics;
import be.kdg.ip3.archportal.analytics.domain.PlayerStatisticsRepository;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.games.shared.AchievementDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GrantedAchievementDto;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformPointsEvent;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrantAchievementSociableTest {
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
    class GrantAchievementTests {
        @Test
        void grantAchievement_validAchievementAndPlayer_addsAchievementAndPublishesEvents() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var externalAchId = "ACH_001";

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            // AANGEPAST: Alleen UUID en String meegeven
            var achievementDto = new GrantedAchievementDto(UUID.randomUUID(), "First Victory");

            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.findAchievementByExternalAchId(externalAchId, gameId.id())).thenReturn(achievementDto);

            // Act
            service.grantAchievement(externalAchId, playerId, gameStatsId);

            // Assert
            verify(playerStatisticsRepository).save(playerStats);
            verify(applicationEventPublisher).publishEvent(any(AddNotificationEvent.class));
            verify(applicationEventPublisher).publishEvent(any(GrantPlatformPointsEvent.class));
        }

        @Test
        void grantAchievement_multipleAchievements_allAddedSuccessfully() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            // AANGEPAST: Alleen UUID en String meegeven voor beide
            var achievementDto1 = new GrantedAchievementDto(UUID.randomUUID(), "First Victory");
            var achievementDto2 = new GrantedAchievementDto(UUID.randomUUID(), "Second Victory");

            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.findAchievementByExternalAchId("ACH_001", gameId.id())).thenReturn(achievementDto1);
            when(gamesApi.findAchievementByExternalAchId("ACH_002", gameId.id())).thenReturn(achievementDto2);

            // Act
            service.grantAchievement("ACH_001", playerId, gameStatsId);
            service.grantAchievement("ACH_002", playerId, gameStatsId);

            // Assert
            verify(playerStatisticsRepository, times(2)).save(playerStats);
            verify(applicationEventPublisher, times(2)).publishEvent(any(AddNotificationEvent.class));
            verify(applicationEventPublisher, times(2)).publishEvent(any(GrantPlatformPointsEvent.class));
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void grantAchievement_profileDoesNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var externalAchId = "ACH_001";

            when(profilesApi.existsById(playerId.id())).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> service.grantAchievement(externalAchId, playerId, gameStatsId))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void grantAchievement_playerStatsDoNotExist_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var externalAchId = "ACH_001";

            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.grantAchievement(externalAchId, playerId, gameStatsId))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void grantAchievement_gameStatisticsNotFound_throwsNotFoundException() {
            // Arrange
            var playerId = new PlayerId(UUID.randomUUID());
            var gameId = new GameId(UUID.randomUUID());
            var gameStatsId = new GameStatisticsId(gameId, playerId);
            var nonExistentGameStatsId = new GameStatisticsId(new GameId(UUID.randomUUID()), playerId);
            var externalAchId = "ACH_001";

            var gameStats = new GameStatistics(gameStatsId);
            var playerStats = new PlayerStatistics(playerId, List.of(gameStats), java.time.Duration.ZERO, null);

            var achievementDto = new GrantedAchievementDto(UUID.randomUUID(), "Test Achievement");

            when(profilesApi.existsById(playerId.id())).thenReturn(true);
            when(playerStatisticsRepository.findById(playerId)).thenReturn(Optional.of(playerStats));
            when(gamesApi.findAchievementByExternalAchId(eq(externalAchId), any(UUID.class)))
                    .thenReturn(achievementDto);

            // Act & Assert
            assertThatThrownBy(() -> service.grantAchievement(externalAchId, playerId, nonExistentGameStatsId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
