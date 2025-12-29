package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.AlreadyFriendException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestAlreadyExistsException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.InvalidFriendRequestException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFriendRequestSociableTest {
    @Mock
    ProfileRepository profileRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    FriendshipRepository friendshipRepository;

    ProfileService service;

    @BeforeEach
    void setUp() {
        service = new ProfileService(profileRepository, null,null, applicationEventPublisher, friendshipRepository);
    }

    @Nested
    class ExceptionFlows {
        @Test
        void createFriendRequest_equalSenderAndReceiverIds_throwsExpected() {
            var profileId = new ProfileId(UUID.randomUUID());
            var gamerTag = "gamerTag";
            var profile = Profile.createProfile(profileId, "Cian", "Van Acker", gamerTag, "cian.vanacker@student.kdg.be","icon");
            
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(profile));
            when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

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
            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", gamerTag, "cian.vanacker@student.kdg.be","icon");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice123", "alice@example.com","icon");
            var existingRequest = new FriendRequest(senderId);
            receiver.addIncomingFriendRequest(existingRequest);

            when(profileRepository.findById(senderId)).thenReturn(Optional.of(sender));
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(receiver));

            assertThatThrownBy(() -> service.createFriendRequest(senderId, gamerTag))
                    .isInstanceOf(FriendRequestAlreadyExistsException.class)
                    .hasMessage("Friend request already exists.");
        }

        @Test
        void createFriendRequest_alreadyFriends_throwsAlreadyFriendException() {
            var senderId = new ProfileId(UUID.randomUUID());
            var receiverId = new ProfileId(UUID.randomUUID());
            var gamerTag = "receiverTag";

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", gamerTag, "cian@example.com","icon");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice123", "alice@example.com","icon");

            when(profileRepository.findById(senderId)).thenReturn(Optional.of(sender));
            when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(receiver));
            when(friendshipRepository.existsBetween(receiverId, senderId)).thenReturn(true);

            assertThatThrownBy(() -> service.createFriendRequest(senderId, gamerTag))
                    .isInstanceOf(AlreadyFriendException.class)
                    .hasMessageContaining(receiver.getGamerTag());

            assertThat(receiver.getIncomingFriendRequests()).isEmpty();

            verify(profileRepository, never()).save(any());
            verify(applicationEventPublisher, never()).publishEvent(any());
        }

    }

    @Test
    void createFriendRequest_createsFriendRequestAndSaves() {
        var senderId = new ProfileId(UUID.randomUUID());
        var receiverId = new ProfileId(UUID.randomUUID());
        var gamerTag = "gamerTag";
        var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", gamerTag, "cian.vanacker@student.kdg.be","icon");
        var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice123", "alice@example.com","icon");

        when(profileRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(profileRepository.findByGamerTag(gamerTag)).thenReturn(Optional.of(receiver));
        
        assertThat(service.createFriendRequest(senderId, gamerTag))
                .returns(senderId, FriendRequest::senderId);
        verify(profileRepository).save(receiver);
        verify(applicationEventPublisher).publishEvent(any(AddNotificationEvent.class));
    }
}
