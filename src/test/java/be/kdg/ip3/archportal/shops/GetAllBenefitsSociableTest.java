package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllBenefitsSociableTest {
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
    class GetAllBenefitsTests {
        @Test
        void getAllBenefits_returnsBenefitsList() {
            // Arrange
            var benefitId1 = new BenefitId(UUID.randomUUID());
            var benefitId2 = new BenefitId(UUID.randomUUID());

            var benefit1 = mock(Benefit.class);
            when(benefit1.getBenefitId()).thenReturn(benefitId1);

            var benefit2 = mock(Benefit.class);
            when(benefit2.getBenefitId()).thenReturn(benefitId2);

            when(benefitRepo.findAll()).thenReturn(List.of(benefit1, benefit2));

            // Act
            var result = service.getAllBenefits();

            // Assert
            assertThat(result).hasSize(2);
            verify(benefitRepo).findAll();
        }

        @Test
        void getAllBenefits_emptyDatabase_returnsEmptyList() {
            // Arrange
            when(benefitRepo.findAll()).thenReturn(List.of());

            // Act
            var result = service.getAllBenefits();

            // Assert
            assertThat(result).isEmpty();
            verify(benefitRepo).findAll();
        }

        @Test
        void getAllBenefits_multipleBenefits_returnsAllBenefits() {
            // Arrange
            var benefitId1 = new BenefitId(UUID.randomUUID());
            var benefitId2 = new BenefitId(UUID.randomUUID());
            var benefitId3 = new BenefitId(UUID.randomUUID());

            var benefit1 = mock(Benefit.class);
            when(benefit1.getBenefitId()).thenReturn(benefitId1);

            var benefit2 = mock(Benefit.class);
            when(benefit2.getBenefitId()).thenReturn(benefitId2);

            var benefit3 = mock(Benefit.class);
            when(benefit3.getBenefitId()).thenReturn(benefitId3);

            when(benefitRepo.findAll()).thenReturn(List.of(benefit1, benefit2, benefit3));

            // Act
            var result = service.getAllBenefits();

            // Assert
            assertThat(result).hasSize(3);
            verify(benefitRepo).findAll();
        }
    }
}

