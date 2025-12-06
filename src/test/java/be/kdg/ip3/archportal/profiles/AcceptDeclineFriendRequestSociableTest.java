package be.kdg.ip3.archportal.profiles;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcceptDeclineFriendRequestSociableTest {
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
    class Accept {
        @Test
        void acceptFriendRequest_validRequest_usersBecomeFriends_requestIsRemoved() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com");

            receiver.addIncomingFriendRequest(new FriendRequest(senderId));

            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.of(sender));
            when(profileRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

            service.acceptFriendRequest(receiverId, "alice");

            assertThat(receiver.getFriends()).contains(senderId);
            assertThat(sender.getFriends()).contains(receiverId);
            assertThat(receiver.getIncomingFriendRequests()).isEmpty();

            verify(profileRepository).save(receiver);
            verify(profileRepository).save(sender);
            verify(applicationEventPublisher).publishEvent(any(AddNotificationEvent.class));
        }
        
        @Test
        void acceptFriendRequest_requestDoesNotExist_throwsExpected() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com");
            
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
    }

    @Nested
    class Decline {
        @Test
        void declineFriendRequest_validRequest_usersAreNotFriends_requestIsRemoved() {
            var receiverId = new ProfileId(UUID.randomUUID());
            var senderId = new ProfileId(UUID.randomUUID());

            var receiver = Profile.createProfile(receiverId, "Cian", "Van Acker", "cian", "cian@gmail.com");
            var sender = Profile.createProfile(senderId, "Alice", "Smith", "alice", "alice@gmail.com");
            
            receiver.addIncomingFriendRequest(new FriendRequest(senderId));
            
            when(profileRepository.findByGamerTag("alice")).thenReturn(Optional.of(sender));
            when(profileRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
            
            service.declineFriendRequest(receiverId, "alice");
            
            assertThat(receiver.getFriends()).doesNotContain(senderId);
            assertThat(sender.getFriends()).doesNotContain(receiverId);
            assertThat(receiver.getIncomingFriendRequests()).isEmpty();
            
            verify(profileRepository).save(receiver);
            verify(profileRepository).save(sender);
            verify(applicationEventPublisher).publishEvent(any(AddNotificationEvent.class));
        }
    }
}
