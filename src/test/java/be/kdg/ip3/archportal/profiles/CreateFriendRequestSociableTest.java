package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.profiles.application.FriendRequestService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.*;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
            var senderId = new ProfileId(UUID.randomUUID());

            assertThatThrownBy(() -> service.createFriendRequest(senderId, senderId))
                    .isInstanceOf(InvalidFriendRequestException.class)
                    .hasMessage("Sender and receiver profiles cannot be the same profile.");
        }

        @Test
        void createFriendRequest_oneProfileDoesntExist_throwsExpected() {
            var senderId = new ProfileId(UUID.randomUUID());
            var receiverId = new ProfileId(UUID.randomUUID());

            when(profileRepository.existsById(senderId)).thenReturn(true);
            when(profileRepository.existsById(receiverId)).thenReturn(false);

            assertThatThrownBy(() -> service.createFriendRequest(senderId, receiverId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Sender or receiver profile does not exist.");
        }

        @Test
        void createFriendRequest_friendRequestAlreadyExists_throwsExpected() {
            var senderId = new ProfileId(UUID.randomUUID());
            var receiverId = new ProfileId(UUID.randomUUID());

            when(profileRepository.existsById(senderId)).thenReturn(true);
            when(profileRepository.existsById(receiverId)).thenReturn(true);

            when(friendRequestRepository.existsPending(senderId, receiverId)).thenReturn(false);
            when(friendRequestRepository.existsPending(receiverId, senderId)).thenReturn(true);

            assertThatThrownBy(() -> service.createFriendRequest(senderId, receiverId))
                    .isInstanceOf(FriendRequestAlreadyExistsException.class)
                    .hasMessage("A pending friend request already exists between these profiles.");
        }
    }

    @Test
    void createFriendRequest_createsFriendRequestAndSaves() {
        var senderId = new ProfileId(UUID.randomUUID());
        var receiverId = new ProfileId(UUID.randomUUID());

        when(profileRepository.existsById(any())).thenReturn(true);
        when(friendRequestRepository.existsPending(any(), any())).thenReturn(false);
        
        assertThat(service.createFriendRequest(senderId, receiverId))
                .returns(senderId, FriendRequest::getSenderId)
                .returns(receiverId, FriendRequest::getReceiverId)
                .returns(FriendRequestState.PENDING, FriendRequest::getState);
        verify(friendRequestRepository).save(any(FriendRequest.class));
    }
}
