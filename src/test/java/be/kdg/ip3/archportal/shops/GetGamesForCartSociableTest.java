package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
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
public class GetGamesForCartSociableTest {
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
    class GetGamesForCartTests {
        @Test
        void getGamesForCart_validCart_returnsGames() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId1 = UUID.randomUUID();
            var gameId2 = UUID.randomUUID();
            var cart = new Cart(profileId);
            cart.addToCart(gameId1);
            cart.addToCart(gameId2);

            var gameDto1 = new GlobalGameDto(gameId1, "Game 1", "Description 1", "image1.jpg", "url1.com", BigDecimal.TEN, be.kdg.ip3.archportal.games.domain.game.GameGenre.ABSTRACT, 4, List.of());
            var gameDto2 = new GlobalGameDto(gameId2, "Game 2", "Description 2", "image2.jpg", "url2.com", BigDecimal.valueOf(20), be.kdg.ip3.archportal.games.domain.game.GameGenre.ADVENTURE, 8, List.of());

            when(gameApi.getGamesByIds(cart.getCartItems())).thenReturn(List.of(gameDto1, gameDto2));

            // Act
            var result = service.getGamesForCart(cart);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).contains(gameDto1, gameDto2);
            verify(gameApi).getGamesByIds(cart.getCartItems());
        }

        @Test
        void getGamesForCart_emptyCart_returnsEmptyList() {
            // Arrange
            var profileId = UUID.randomUUID();
            var cart = new Cart(profileId);

            when(gameApi.getGamesByIds(cart.getCartItems())).thenReturn(List.of());

            // Act
            var result = service.getGamesForCart(cart);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        void getGamesForCart_singleGame_returnsSingleGame() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();
            var cart = new Cart(profileId);
            cart.addToCart(gameId);

            var gameDto = new GlobalGameDto(gameId, "Game 1", "Description 1", "image1.jpg", "url1.com", BigDecimal.TEN, be.kdg.ip3.archportal.games.domain.game.GameGenre.ABSTRACT, 4, List.of());

            when(gameApi.getGamesByIds(cart.getCartItems())).thenReturn(List.of(gameDto));

            // Act
            var result = service.getGamesForCart(cart);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.getFirst()).isEqualTo(gameDto);
        }
    }
}

