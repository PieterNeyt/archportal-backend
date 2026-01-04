package be.kdg.ip3.archportal.shops;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import be.kdg.ip3.archportal.shops.api.dto.BenefitDto;
import be.kdg.ip3.archportal.shops.application.ShopService;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitId;
import be.kdg.ip3.archportal.shops.domain.cart.CartRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.Benefit;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitRepository;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitType;
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
public class GetProfileDiscountsSociableTest {
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
    class GetProfileDiscountsTests {
        @Test
        void getProfileDiscounts_validProfile_returnsGameDiscounts() {
            // Arrange
            var profileId = UUID.randomUUID();
            var bId1 = UUID.randomUUID();
            var bId2 = UUID.randomUUID();

            var benefit1 = new Benefit(new BenefitId(bId1), BenefitType.GAME_DISCOUNT, "Discount 1", "Description", 100, "{}");
            var benefit2 = new Benefit(new BenefitId(bId2), BenefitType.GAME_DISCOUNT, "Discount 2", "Description", 200, "{}");

            when(profilesApi.getProfileBenefitsByProfileId(profileId)).thenReturn(java.util.Set.of(bId1, bId2));
            when(benefitRepo.findAllByIdIn(any())).thenReturn(List.of(benefit1, benefit2));

            // Act
            var result = service.getProfileDiscounts(profileId);

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).extracting(BenefitDto::name).containsExactlyInAnyOrder("Discount 1", "Discount 2");

            verify(profilesApi).getProfileBenefitsByProfileId(profileId);
        }

        @Test
        void getProfileDiscounts_filterNonDiscountBenefits_returnsOnlyDiscounts() {
            // Arrange
            var profileId = UUID.randomUUID();
            var bId1 = UUID.randomUUID();
            var bId2 = UUID.randomUUID();
            var bId3 = UUID.randomUUID();

            var benefit1 = new Benefit(new BenefitId(bId1), BenefitType.GAME_DISCOUNT, "Discount 1", "Description", 100, "{}");
            var benefit2 = new Benefit(new BenefitId(bId2), BenefitType.GAME_DISCOUNT, "Discount 2", "Description", 200, "{}");
            var benefit3 = new Benefit(new BenefitId(bId3), BenefitType.GAME_DISCOUNT, "Discount 2", "Description", 150, "{}");

            when(profilesApi.getProfileBenefitsByProfileId(profileId)).thenReturn(java.util.Set.of(bId1, bId2, bId3));
            when(benefitRepo.findAllByIdIn(any())).thenReturn(List.of(benefit1, benefit2, benefit3));

            // Act
            var result = service.getProfileDiscounts(profileId);

            // Assert
            assertThat(result).hasSize(3);
            assertThat(result).extracting(BenefitDto::type).containsOnly(BenefitType.GAME_DISCOUNT);
            assertThat(result).extracting(BenefitDto::name).doesNotContain("Wrong Type");
        }

        @Test
        void getProfileDiscounts_noBenefits_returnsEmptyList() {
            // Arrange
            var profileId = UUID.randomUUID();

            when(profilesApi.getProfileBenefitsByProfileId(profileId)).thenReturn(java.util.Set.of());
            when(benefitRepo.findAllByIdIn(any())).thenReturn(List.of());

            // Act
            var result = service.getProfileDiscounts(profileId);

            // Assert
            assertThat(result).isEmpty();
        }
    }
}

