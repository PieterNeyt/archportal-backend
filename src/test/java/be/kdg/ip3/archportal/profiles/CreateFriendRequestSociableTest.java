package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestAlreadyExistsException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.InvalidFriendRequestException;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFriendRequestSociableTest {
    @Mock
    ProfileRepository profileRepository;

    ProfileService service;

    @BeforeEach
    void setUp() {
        service = new ProfileService(profileRepository, null, null);
    }

    @Nested
    class ExceptionFlows {
        @Test
        void createFriendRequest_equalSenderAndReceiverIds_throwsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";
            var profile = Profile.createProfile(profileId, "Cian", "Van Acker", gamerTag, "cian.vanacker@student.kdg.be");
            
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(profile));

            assertThatThrownBy(() -> service.createFriendRequest(profileId, gamerTag))
                    .isInstanceOf(InvalidFriendRequestException.class)
                    .hasMessage("Sender and receiver profiles cannot be the same profile.");
        }

        @Test
        void createFriendRequest_oneProfileDoesntExist_throwsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";

            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.createFriendRequest(profileId, gamerTag))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Profile with gamertag gamerTag is not found.");
        }

        @Test
        void createFriendRequest_friendRequestAlreadyExists_throwsExpected() {
            var senderId = new ProfileId(UUID.randomUUID());
            var receiverId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";
            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", gamerTag, "cian.vanacker@student.kdg.be");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice123", "alice@example.com");
            var existingRequest = new FriendRequest(senderId);
            receiver.addIncomingFriendRequest(existingRequest);

            when(profileRepository.findById(senderId)).thenReturn(Optional.of(sender));
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(receiver));

            assertThatThrownBy(() -> service.createFriendRequest(senderId, gamerTag))
                    .isInstanceOf(FriendRequestAlreadyExistsException.class)
                    .hasMessage("A pending friend request already exists between these profiles.");
        }
    }

    @Test
    void createFriendRequest_createsFriendRequestAndSaves() {
        var senderId = new ProfileId(UUID.randomUUID());
        var receiverId = new ProfileId(UUID.randomUUID());
        var gamerTag = "gamerTag";
        var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", gamerTag, "cian.vanacker@student.kdg.be");
        var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice123", "alice@example.com");

        when(profileRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(receiver));
        
        assertThat(service.createFriendRequest(senderId, gamerTag))
                .returns(senderId, FriendRequest::senderId);
    }
}
