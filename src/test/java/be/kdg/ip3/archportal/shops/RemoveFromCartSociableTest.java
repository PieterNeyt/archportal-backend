package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.NotFoundException;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RemoveFromCartSociableTest {
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
    class RemoveFromCartTests {
        @Test
        void removeFromCart_gameInCart_removesGameFromCart() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();
            var cart = new Cart(profileId);
            cart.addToCart(gameId);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));
            when(cartRepo.save(any(Cart.class))).thenReturn(cart);

            // Act
            var result = service.removeFromCart(profileId, gameId);

            // Assert
            assertThat(result.getCartItems()).doesNotContain(gameId);
            verify(cartRepo).save(any(Cart.class));
        }

        @Test
        void removeFromCart_multipleGames_onlyRemovesSelected() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId1 = UUID.randomUUID();
            var gameId2 = UUID.randomUUID();
            var cart = new Cart(profileId);
            cart.addToCart(gameId1);
            cart.addToCart(gameId2);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));
            when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            service.removeFromCart(profileId, gameId1);

            // Assert
            assertThat(cart.getCartItems()).contains(gameId2);
            assertThat(cart.getCartItems()).doesNotContain(gameId1);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void removeFromCart_cartDoesNotExist_throwsNotFoundException() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.removeFromCart(profileId, gameId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("No cart found for user with id: " + profileId);
        }

        @Test
        void removeFromCart_gameNotInCart_throwsException() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId1 = UUID.randomUUID();
            var gameId2 = UUID.randomUUID();
            var cart = new Cart(profileId);
            cart.addToCart(gameId1);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));

            // Act & Assert
            assertThatThrownBy(() -> service.removeFromCart(profileId, gameId2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Game is not in cart");
        }
    }
}

