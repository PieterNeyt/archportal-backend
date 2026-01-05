package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.application.command.AcquirePlatformPointsCommand;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.shops.shared.GlobalBenefitDto;
import be.kdg.ip3.archportal.shops.shared.ShopsApi;
import be.kdg.ip3.archportal.shops.domain.benefit.BenefitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceUnitTest {
    @Mock
    ProfileRepository profileRepository;

    @Mock
    FriendshipRepository friendshipRepository;

    @Mock
    ShopsApi shopsApi;

    @Mock
    GamesApi gamesApi;

    @Mock
    ApplicationEventPublisher eventPublisher;

    ProfileService service;

    @BeforeEach
    void setUp() {
        service = new ProfileService(profileRepository, gamesApi, shopsApi, eventPublisher, friendshipRepository);
    }

    @Nested
    class ToggleBenefit {
        @Test
        void usernameColor_activatesAndDeactivates() {
            var id = new ProfileId(UUID.randomUUID());
            var profile = Profile.createProfile(id, "First", "Last", "tag1", "a@b.com", "icon");

            when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

            var benefitId = UUID.randomUUID();
            when(shopsApi.getBenefitById(benefitId)).thenReturn(new GlobalBenefitDto(benefitId, BenefitType.USERNAME_COLOR, "name", "desc", 0, null));

            service.toggleBenefit(id, benefitId);
            var after = profileRepository.findById(id).orElseThrow();
            assertThat(after.getActiveUsernameColorId()).isEqualTo(benefitId);

            service.toggleBenefit(id, benefitId);
            var after2 = profileRepository.findById(id).orElseThrow();
            assertThat(after2.getActiveUsernameColorId()).isNull();
        }
    }

    @Nested
    class PlatformPoints {
        @Test
        void acquireAndGetPoints() {
            var id = new ProfileId(UUID.randomUUID());
            var profile = Profile.createProfile(id, "First", "Last", "tag2", "a@b.com", "icon");
            when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

            service.aquirePlatformPoints(new AcquirePlatformPointsCommand(id, 50));
            assertThat(service.getPlatformPoints(id)).isEqualTo(50);

            service.aquirePlatformPoints(new AcquirePlatformPointsCommand(id, 25));
            assertThat(service.getPlatformPoints(id)).isEqualTo(75);
        }
    }

    @Nested
    class Favorites {
        @Test
        void addAndRemoveFavorite() {
            var id = new ProfileId(UUID.randomUUID());
            var profile = Profile.createProfile(id, "First", "Last", "tag3", "a@b.com", "icon");
            when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

            var gameId = UUID.randomUUID();
            profile.acquireGame(gameId);
            when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

            service.addFavoriteToGame(id, gameId);
            var fetched = profileRepository.findById(id).orElseThrow();
            assertThat(fetched.getGames()).anyMatch(g -> g.getGameId().equals(gameId) && g.isFavorite());

            service.removeFavoriteFromGame(id, gameId);
            var fetched2 = profileRepository.findById(id).orElseThrow();
            assertThat(fetched2.getGames()).anyMatch(g -> g.getGameId().equals(gameId) && !g.isFavorite());
        }
    }

    @Nested
    class FriendshipCheck {
        @Test
        void getIsFriends_behaviour() {
            var a = new ProfileId(UUID.randomUUID());
            var b = new ProfileId(UUID.randomUUID());
            var profileB = Profile.createProfile(b, "B", "B", "b_tag", "b@b.com", "icon");

            assertThat(service.getIsFriends(a, a)).isFalse();

            when(profileRepository.findAllFriends(a)).thenReturn(List.of());
            assertThat(service.getIsFriends(a, b)).isFalse();

            when(profileRepository.findAllFriends(a)).thenReturn(List.of(profileB));
            assertThat(service.getIsFriends(a, b)).isTrue();
        }
    }
}
