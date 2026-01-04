package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformPointsEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.cart.Cart;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitRepository;
import be.kdg.ip3.archportal.shops.domain.mollie.IMollieService;
import be.kdg.ip3.archportal.shops.domain.order.Order;
import be.kdg.ip3.archportal.shops.domain.order.OrderLine;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyPaymentSociableTest {
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
    class VerifyPaymentTests {
        @Test
        void verifyPayment_successfulPayment_completesOrderAndPublishesEvent() {
            // Arrange
            var orderId = UUID.randomUUID();
            var profileId = UUID.randomUUID();
            var paymentId = "tr_test123";
            var gameId = UUID.randomUUID();

            var order = mock(Order.class);
            when(order.getProfileId()).thenReturn(profileId);
            when(order.getPaymentId()).thenReturn(paymentId);
            when(order.isCompleted()).thenReturn(false);
            when(order.getOrderLines()).thenReturn(List.of(new OrderLine(gameId, BigDecimal.TWO)));
            when(order.totalPrice()).thenReturn(java.math.BigDecimal.valueOf(100));

            var cart = new Cart(profileId);
            cart.addToCart(gameId);

            when(orderRepo.findById(orderId)).thenReturn(order);
            when(cartRepo.findByProfileId(profileId)).thenReturn(Optional.of(cart));
            when(mollieService.verifyPayment(paymentId)).thenReturn(true);

            // Act
            var result = service.verifyPayment(orderId);

            // Assert
            assertThat(result).isTrue();
            verify(order).markAsCompleted();
            verify(orderRepo).save(order);
            verify(cartRepo).delete(cart);
            verify(applicationEventPublisher).publishEvent(any(GrantPlatformPointsEvent.class));
        }

        @Test
        void verifyPayment_failedPayment_doesNotCompleteOrder() {
            // Arrange
            var orderId = UUID.randomUUID();
            var paymentId = "tr_test123";

            var order = mock(Order.class);
            when(order.getPaymentId()).thenReturn(paymentId);

            when(orderRepo.findById(orderId)).thenReturn(order);
            when(mollieService.verifyPayment(paymentId)).thenReturn(false);

            // Act
            var result = service.verifyPayment(orderId);

            // Assert
            assertThat(result).isFalse();
            verify(order, never()).markAsCompleted();
            verify(orderRepo, never()).save(order);
            verify(applicationEventPublisher, never()).publishEvent(any());
        }

        @Test
        void verifyPayment_orderAlreadyCompleted_doesNotReprocess() {
            // Arrange
            var orderId = UUID.randomUUID();
            var paymentId = "tr_test123";

            var order = mock(Order.class);
            when(order.getPaymentId()).thenReturn(paymentId);
            when(order.isCompleted()).thenReturn(true);

            when(orderRepo.findById(orderId)).thenReturn(order);
            when(mollieService.verifyPayment(paymentId)).thenReturn(true);

            // Act
            var result = service.verifyPayment(orderId);

            // Assert
            assertThat(result).isTrue();
            verify(order, never()).markAsCompleted();
            verify(profilesApi, never()).addGamesToLibrary(any(UUID.class), any());
        }
    }
}

