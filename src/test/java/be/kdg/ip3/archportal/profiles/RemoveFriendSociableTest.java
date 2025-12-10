package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendship.Friendship;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RemoveFriendSociableTest {
    @Mock
    ProfileRepository profileRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    FriendshipRepository friendshipRepository;

    ProfileService service;

    @BeforeEach
    void setUp() {
        service = new ProfileService(profileRepository, null, applicationEventPublisher, friendshipRepository);
    }

    @Nested
    class ExceptionFlows {
        @Test
        void removeFriend_profileDoesNotExists_ThrowsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());

            when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeFriend(profileId, "gamertag"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Profile [" + profileId.id() + "] not found");
        }

        @Test
        void removeFriend_friendAccountDoesNotExist_throwsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";
            var profile = Profile.createProfile(profileId, "Cian", "Van Acker", "cian", "cian.vanacker@student.kdg.be","icon");

            when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeFriend(profileId, gamerTag))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Profile with gamertag " + gamerTag + " is not found.");
        }

        @Test
        void removeFriend_givenAccountIsNotAFriend_throwsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());
            var friendId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";
            var profile = Profile.createProfile(profileId, "Cian", "Van Acker", "cian", "cian.vanacker@student.kdg.be","icon");
            var friend = Profile.createProfile(friendId, "Alice", "Smith", gamerTag, "alice@example.com","icon");

            when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(friend));
            when(friendshipRepository.findBetween(profileId, friendId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeFriend(profileId, gamerTag))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("You are not friends with " + gamerTag + ".");
        }
    }

    @Test
    void removeFriend_deletesFriendAndSaves() {
        var profileId = new ProfileId(UUID.randomUUID());
        var friendId = new ProfileId(UUID.randomUUID());
        var gamerTag = "gamerTag";
        var profile = Profile.createProfile(profileId, "Cian", "Van Acker", "cian", "cian.vanacker@student.kdg.be","icon");
        var friend = Profile.createProfile(friendId, "Alice", "Smith", gamerTag, "alice@example.com","icon");

        var friendShip = new Friendship(profileId, friendId);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(friend));
        when(friendshipRepository.findBetween(profileId, friendId)).thenReturn(Optional.of(friendShip));

        service.removeFriend(profileId, gamerTag);

        verify(friendshipRepository).delete(friendShip);
        verify(profileRepository, never()).save(any());
    }
}
