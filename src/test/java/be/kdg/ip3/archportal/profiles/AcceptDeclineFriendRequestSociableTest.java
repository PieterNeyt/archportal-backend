package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.AlreadyFriendException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendship.Friendship;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.profiles.shared.FriendShipCreatedEvent;
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
public class AcceptDeclineFriendRequestSociableTest {
    @Mock
    ProfileRepository profileRepository;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    FriendshipRepository friendshipRepository;

    ProfileService service;

    @BeforeEach
    void setUp() {
        service = new ProfileService(profileRepository, null, null,applicationEventPublisher, friendshipRepository);
    }

    @Nested
    class Accept {
        @Test
        void acceptFriendRequest_validRequest_usersBecomeFriends_requestIsRemoved() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com","");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com","");

            receiver.addIncomingFriendRequest(new FriendRequest(senderId));

            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.of(sender));
            when(profileRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
            when(friendshipRepository.existsBetween(receiverId, senderId)).thenReturn(false);

            service.acceptFriendRequest(receiverId, "alice");

            assertThat(receiver.getIncomingFriendRequests()).isEmpty();

            verify(friendshipRepository).save(any(Friendship.class));
            verify(profileRepository).save(receiver);
            verify(applicationEventPublisher).publishEvent(any(AddNotificationEvent.class));
            verify(applicationEventPublisher).publishEvent(any(FriendShipCreatedEvent.class));
        }

        @Test
        void acceptFriendRequest_requestDoesNotExist_throwsExpected() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com","icon");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com","icon");

            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.of(sender));
            when(profileRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

            assertThatThrownBy(() -> service.acceptFriendRequest(receiverId, "alice"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Friend request not found.");
        }

        @Test
        void acceptFriendRequest_senderDoesNotExist_throwsExpected() {
            var receiverId = new ProfileId(UUID.randomUUID());

            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.acceptFriendRequest(receiverId, "alice"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Profile with gamertag alice is not found.");
        }

        @Test
        void acceptFriendRequest_alreadyFriends_throwsAlreadyFriendException_requestRemoved() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());
            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com","icon");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com","icon");

            receiver.addIncomingFriendRequest(new FriendRequest(senderId));

            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.of(sender));
            when(profileRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
            when(friendshipRepository.existsBetween(receiverId, senderId)).thenReturn(true);

            assertThatThrownBy(() -> service.acceptFriendRequest(receiverId, "alice"))
                    .isInstanceOf(AlreadyFriendException.class)
                    .hasMessageContaining(receiver.getGamerTag());

            assertThat(receiver.getIncomingFriendRequests()).isEmpty();

            verify(profileRepository).save(receiver);
            verify(friendshipRepository, never()).save(any(Friendship.class));
        }
    }

    @Nested
    class Decline {
        @Test
        void declineFriendRequest_validRequest_usersAreNotFriends_requestIsRemoved() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com","icon");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com","icon");

            receiver.addIncomingFriendRequest(new FriendRequest(senderId));

            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.of(sender));
            when(profileRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

            service.declineFriendRequest(receiverId, "alice");

            assertThat(receiver.getIncomingFriendRequests()).isEmpty();

            verify(profileRepository).save(receiver);
            verify(applicationEventPublisher).publishEvent(any(AddNotificationEvent.class));
        }
    }
}
