package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.profiles.application.FriendRequestService;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestAlreadyExistsException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.InvalidFriendRequestException;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFriendRequestSociableTest {
    @Mock
    ProfileRepository profileRepository;
    @Mock
    FriendRequestRepository friendRequestRepository;

    FriendRequestService service;

    @BeforeEach
    void setUp() {
        service = new FriendRequestService(friendRequestRepository, profileRepository);
    }

    @Nested
    class ExceptionFlows {
        @Test
        void createFriendRequest_equalSenderAndReceiverIds_throwsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";
            var profile = new Profile(profileId, null, 0, "Van Acker", null, gamerTag, null, "Cian", null);
            
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
            var profile = new Profile(receiverId, null, 0, "Van Acker", null, gamerTag, null, "Cian", null);

            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(profile));

            when(profileRepository.existsById(senderId)).thenReturn(true);

            when(friendRequestRepository.existsPending(senderId, receiverId)).thenReturn(false);
            when(friendRequestRepository.existsPending(receiverId, senderId)).thenReturn(true);

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
        var profile = new Profile(receiverId, null, 0, "Van Acker", null, gamerTag, null, "Cian", null);

        when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(profile));

        when(profileRepository.existsById(any())).thenReturn(true);
        when(friendRequestRepository.existsPending(any(), any())).thenReturn(false);
        
        assertThat(service.createFriendRequest(senderId, gamerTag))
                .returns(senderId, FriendRequest::getSenderId)
                .returns(receiverId, FriendRequest::getReceiverId)
                .returns(FriendRequestState.PENDING, FriendRequest::getState);
        verify(friendRequestRepository).save(any(FriendRequest.class));
    }
}
