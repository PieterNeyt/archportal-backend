package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddToCartSociableTest {
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
    class AddToCartTests {
        @Test
        void addToCart_validGame_addsGameToCart() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();
            var cart = new Cart(profileId);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));
            when(cartRepo.save(any(Cart.class))).thenReturn(cart);

            // Act
            var result = service.addToCart(profileId, gameId);

            // Assert
            assertThat(result.getCartItems()).contains(gameId);
            verify(cartRepo).save(any(Cart.class));
            verify(profilesApi).checkAlreadyOwnsGame(profileId, gameId);
        }

        @Test
        void addToCart_multipleGames_allAddedToCart() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId1 = UUID.randomUUID();
            var gameId2 = UUID.randomUUID();
            var cart = new Cart(profileId);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));
            when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            service.addToCart(profileId, gameId1);
            service.addToCart(profileId, gameId2);

            // Assert
            verify(cartRepo, times(2)).save(any(Cart.class));
            verify(profilesApi, times(2)).checkAlreadyOwnsGame(any(UUID.class), any(UUID.class));
        }

        @Test
        void addToCart_createNewCartIfNotExists() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();
            var newCart = new Cart(profileId);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.empty());
            when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

            // Act
            service.addToCart(profileId, gameId);

            // Assert
            verify(cartRepo, times(2)).save(any(Cart.class));
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void addToCart_gameAlreadyInCart_throwsException() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();
            var cart = new Cart(profileId);
            cart.addToCart(gameId);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));

            // Act & Assert
            assertThatThrownBy(() -> service.addToCart(profileId, gameId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void addToCart_profileAlreadyOwnsGame_throwsException() {
            // Arrange
            var profileId = UUID.randomUUID();
            var gameId = UUID.randomUUID();
            var cart = new Cart(profileId);

            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));
            doThrow(new IllegalArgumentException("Profile already owns this game"))
                    .when(profilesApi).checkAlreadyOwnsGame(profileId, gameId);

            // Act & Assert
            assertThatThrownBy(() -> service.addToCart(profileId, gameId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

