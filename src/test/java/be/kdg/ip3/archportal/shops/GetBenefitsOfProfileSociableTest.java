package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
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
public class GetBenefitsOfProfileSociableTest {
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
    class GetBenefitsOfProfileTests {
        @Test
        void getBenefitsOfProfile_validProfile_returnsBenefitsList() {
            // Arrange
            var profileId = UUID.randomUUID();
            var benefitId1 = UUID.randomUUID();
            var benefitId2 = UUID.randomUUID();

            var benefit1 = mock(Benefit.class);
            var benefit2 = mock(Benefit.class);

            when(profilesApi.getProfileBenefitsByProfileId(profileId)).thenReturn(java.util.Set.of(benefitId1, benefitId2));
            when(benefitRepo.findAllByIdIn(any())).thenReturn(List.of(benefit1, benefit2));

            // Act
            var result = service.getBenefitsOfProfile(profileId);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).contains(benefit1, benefit2);
            verify(profilesApi).getProfileBenefitsByProfileId(profileId);
            verify(benefitRepo).findAllByIdIn(any());
        }

        @Test
        void getBenefitsOfProfile_noProfileBenefits_returnsEmptyList() {
            // Arrange
            var profileId = UUID.randomUUID();

            when(profilesApi.getProfileBenefitsByProfileId(profileId)).thenReturn(java.util.Set.of());
            when(benefitRepo.findAllByIdIn(any())).thenReturn(List.of());

            // Act
            var result = service.getBenefitsOfProfile(profileId);

            // Assert
            assertThat(result).isEmpty();
            verify(profilesApi).getProfileBenefitsByProfileId(profileId);
            verify(benefitRepo).findAllByIdIn(any());
        }

        @Test
        void getBenefitsOfProfile_multipleBenefits_returnsAllBenefits() {
            // Arrange
            var profileId = UUID.randomUUID();
            var benefitId1 = UUID.randomUUID();
            var benefitId2 = UUID.randomUUID();
            var benefitId3 = UUID.randomUUID();

            var benefit1 = mock(Benefit.class);
            var benefit2 = mock(Benefit.class);
            var benefit3 = mock(Benefit.class);

            when(profilesApi.getProfileBenefitsByProfileId(profileId)).thenReturn(java.util.Set.of(benefitId1, benefitId2, benefitId3));
            when(benefitRepo.findAllByIdIn(any())).thenReturn(List.of(benefit1, benefit2, benefit3));

            // Act
            var result = service.getBenefitsOfProfile(profileId);

            // Assert
            assertThat(result).hasSize(3);
            assertThat(result).contains(benefit1, benefit2, benefit3);
        }
    }
}

