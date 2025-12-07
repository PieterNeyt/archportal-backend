package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveFriendSociableTest {
    @Mock
    ProfileRepository profileRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    ProfileService service;

    @BeforeEach
    void setUp() {
        service = new ProfileService(profileRepository, null, applicationEventPublisher);
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
            var profile = Profile.createProfile(profileId, "Cian", "Van Acker", "cian", "cian.vanacker@student.kdg.be");

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
            var profile = Profile.createProfile(profileId, "Cian", "Van Acker", "cian", "cian.vanacker@student.kdg.be");
            var friend = Profile.createProfile(friendId, "Alice", "Smith", gamerTag, "alice@example.com");

            when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(friend));

            assertThatThrownBy(() -> service.removeFriend(profileId, gamerTag))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("You are not friends.");
        }
    }

    @Test
    void removeFriend_deletesFriendAndSaves() {
        var profileId = new ProfileId(UUID.randomUUID());
        var friendId = new ProfileId(UUID.randomUUID());
        var gamerTag = "gamerTag";
        var profile = Profile.createProfile(profileId, "Cian", "Van Acker", "cian", "cian.vanacker@student.kdg.be");
        var friend = Profile.createProfile(friendId, "Alice", "Smith", gamerTag, "alice@example.com");

        profile.addFriend(friendId);
        friend.addFriend(profileId);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(friend));

        service.removeFriend(profileId, gamerTag);

        assertThat(profile.getFriends()).doesNotContain(friendId);
        assertThat(friend.getFriends()).doesNotContain(profileId);

        verify(profileRepository).save(profile);
        verify(profileRepository).save(friend);
    }
}
