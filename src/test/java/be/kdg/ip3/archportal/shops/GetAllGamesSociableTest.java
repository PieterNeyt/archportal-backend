package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitRepository;
import be.kdg.ip3.archportal.shops.domain.mollie.IMollieService;
import be.kdg.ip3.archportal.shops.domain.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllGamesSociableTest {
    @Mock
    GamesApi gameApi;
    @Mock
    ProfilesApi profilesApi;
    @Mock
    CartRepository cartRepo;
    @Mock
    OrderRepository orderRepo;
    @Mock
    BenefitRepository benefitRepo;
    @Mock
    IMollieService mollieService;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    ShopService service;
    private static final int PLATFORM_POINTS_MULTIPLIER = 10;

    @BeforeEach
    void setUp() {
        service = new ShopService(gameApi, profilesApi, cartRepo, orderRepo, benefitRepo, mollieService, applicationEventPublisher, PLATFORM_POINTS_MULTIPLIER);
    }

    @Nested
    class GetAllGamesTests {
        @Test
        void getAllGames_returnsAllAvailableGames() {
            // Arrange
            var gameId1 = UUID.randomUUID();
            var gameId2 = UUID.randomUUID();
            var gameId3 = UUID.randomUUID();

            var gameDto1 = new GlobalGameDto(gameId1, "Game 1", "Description 1", "image1.jpg", "url1.com", BigDecimal.TEN, GameGenre.ABSTRACT, 4, List.of());
            var gameDto2 = new GlobalGameDto(gameId2, "Game 2", "Description 2", "image2.jpg", "url2.com", BigDecimal.valueOf(20), GameGenre.ADVENTURE, 8, List.of());
            var gameDto3 = new GlobalGameDto(gameId3, "Game 3", "Description 3", "image3.jpg", "url3.com", BigDecimal.valueOf(30), GameGenre.PARTY, 6, List.of());

            when(gameApi.getAllGames()).thenReturn(List.of(gameDto1, gameDto2, gameDto3));

            // Act
            var result = service.getAllGames();

            // Assert
            assertThat(result).hasSize(3);
            assertThat(result).contains(gameDto1, gameDto2, gameDto3);
            verify(gameApi).getAllGames();
        }

        @Test
        void getAllGames_emptyDatabase_returnsEmptyList() {
            // Arrange
            when(gameApi.getAllGames()).thenReturn(List.of());

            // Act
            var result = service.getAllGames();

            // Assert
            assertThat(result).isEmpty();
            verify(gameApi).getAllGames();
        }

        @Test
        void getAllGames_multipleGames_returnsInCorrectOrder() {
            // Arrange
            var gameId1 = UUID.randomUUID();
            var gameId2 = UUID.randomUUID();

            var gameDto1 = new GlobalGameDto(gameId1, "Game 1", "Description 1", "image1.jpg", "url1.com", BigDecimal.TEN, GameGenre.ABSTRACT, 4, List.of());
            var gameDto2 = new GlobalGameDto(gameId2, "Game 2", "Description 2", "image2.jpg", "url2.com", BigDecimal.valueOf(20), GameGenre.ADVENTURE, 8, List.of());

            when(gameApi.getAllGames()).thenReturn(List.of(gameDto1, gameDto2));

            // Act
            var result = service.getAllGames();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result.get(0)).isEqualTo(gameDto1);
            assertThat(result.get(1)).isEqualTo(gameDto2);
        }
    }
}

