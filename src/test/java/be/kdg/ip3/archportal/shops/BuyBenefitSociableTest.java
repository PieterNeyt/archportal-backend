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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuyBenefitSociableTest {
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
    class BuyBenefitTests {
        @Test
        void buyBenefit_validBenefit_addsBenefitToProfile() {
            // Arrange
            var profileId = UUID.randomUUID();
            var benefitId = new BenefitId(UUID.randomUUID());
            var pointCost = 500;

            var benefit = mock(Benefit.class);
            when(benefit.getBenefitId()).thenReturn(benefitId);
            when(benefit.getPointCost()).thenReturn(pointCost);

            when(benefitRepo.findById(benefitId)).thenReturn(java.util.Optional.of(benefit));
            when(profilesApi.addBenefitToProfile(profileId, benefitId.id(), pointCost)).thenReturn(100);

            // Act
            var result = service.buyBenefit(profileId, benefitId);

            // Assert
            assertThat(result).isEqualTo(100);
            verify(benefitRepo).findById(benefitId);
            verify(profilesApi).addBenefitToProfile(profileId, benefitId.id(), pointCost);
        }

        @Test
        void buyBenefit_multipleBenefits_eachAddedSeparately() {
            // Arrange
            var profileId = UUID.randomUUID();
            var benefitId1 = new BenefitId(UUID.randomUUID());
            var benefitId2 = new BenefitId(UUID.randomUUID());

            var benefit1 = mock(Benefit.class);
            when(benefit1.getBenefitId()).thenReturn(benefitId1);
            when(benefit1.getPointCost()).thenReturn(500);

            var benefit2 = mock(Benefit.class);
            when(benefit2.getBenefitId()).thenReturn(benefitId2);
            when(benefit2.getPointCost()).thenReturn(1000);

            when(benefitRepo.findById(benefitId1)).thenReturn(java.util.Optional.of(benefit1));
            when(benefitRepo.findById(benefitId2)).thenReturn(java.util.Optional.of(benefit2));
            when(profilesApi.addBenefitToProfile(profileId, benefitId1.id(), 500)).thenReturn(100);
            when(profilesApi.addBenefitToProfile(profileId, benefitId2.id(), 1000)).thenReturn(50);

            // Act
            var result1 = service.buyBenefit(profileId, benefitId1);
            var result2 = service.buyBenefit(profileId, benefitId2);

            // Assert
            assertThat(result1).isEqualTo(100);
            assertThat(result2).isEqualTo(50);
            verify(profilesApi, times(2)).addBenefitToProfile(any(UUID.class), any(UUID.class), anyInt());
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void buyBenefit_benefitNotFound_throwsException() {
            // Arrange
            var profileId = UUID.randomUUID();
            var benefitId = new BenefitId(UUID.randomUUID());

            when(benefitRepo.findById(benefitId)).thenReturn(java.util.Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.buyBenefit(profileId, benefitId))
                    .isInstanceOf(Exception.class);
        }
    }
}

