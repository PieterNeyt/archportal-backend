package be.kdg.ip3.archportal.communications.chatroom;

import be.kdg.ip3.archportal.communications.application.ChatRoomService;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.communications.domain.chatroom.Message;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.profiles.shared.FriendShipCreatedEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfileDto;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomSociableTest {
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    ProfilesApi profilesApi;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    ChatRoomService service;

    @BeforeEach
    void setUp() {
        service = new ChatRoomService(chatRoomRepository, profilesApi, applicationEventPublisher);
    }

    @Nested
    class ExceptionFlows {
        @Test
        void createChatRoom_creatorDoesNotExist_throwsNotFoundException() {
            var creatorId = UUID.randomUUID();
            var gamerTags = List.of("a", "b");

            when(profilesApi.existsById(creatorId)).thenReturn(false);

            assertThatThrownBy(() -> service.createChatRoom(gamerTags, creatorId))
                    .isInstanceOf(be.kdg.ip3.archportal.communications.domain.NotFoundException.class)
                    .hasMessageContaining("Creator profile does not exist");

            verify(chatRoomRepository, never()).save(any());
        }

        @Test
        void createChatRoom_missingGamerTag_throwsNotFoundException() {
            var creatorId = UUID.randomUUID();
            var gamerTags = List.of("a", "b");

            when(profilesApi.existsById(creatorId)).thenReturn(true);
            when(profilesApi.getProfilesFromGamerTags(gamerTags)).thenReturn(List.of(
                    new ProfileDto(UUID.randomUUID(), "f", "l", "icon", "a")
            ));

            assertThatThrownBy(() -> service.createChatRoom(gamerTags, creatorId))
                    .isInstanceOf(be.kdg.ip3.archportal.communications.domain.NotFoundException.class)
                    .hasMessageContaining("gamerTags do not exist");

            verify(chatRoomRepository, never()).save(any());
        }

        @Test
        void createChatRoom_notFriends_throwsIllegalArgumentException() {
            var creatorId = UUID.randomUUID();
            var profileAId = UUID.randomUUID();
            var profileBId = UUID.randomUUID();
            var gamerTags = List.of("a", "b");

            when(profilesApi.existsById(creatorId)).thenReturn(true);
            when(profilesApi.getProfilesFromGamerTags(gamerTags)).thenReturn(List.of(
                    new ProfileDto(profileAId, "f", "l", "icon", "a"),
                    new ProfileDto(profileBId, "f2", "l2", "icon2", "b")
            ));
            when(profilesApi.getFriendIdsWithCreator(creatorId, List.of(profileAId, profileBId))).thenReturn(List.of(profileAId));

            assertThatThrownBy(() -> service.createChatRoom(gamerTags, creatorId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("You are not friends");

            verify(chatRoomRepository, never()).save(any());
        }

        @Test
        void getChatRoom_profileDoesNotExist_throwsNotFoundException() {
            var profileId = UUID.randomUUID();
            var roomId = new ChatRoomId(UUID.randomUUID());

            when(profilesApi.existsById(profileId)).thenReturn(false);

            assertThatThrownBy(() -> service.getChatRoom(roomId, profileId))
                    .isInstanceOf(be.kdg.ip3.archportal.communications.domain.NotFoundException.class)
                    .hasMessageContaining("Profile does not exist");
        }

        @Test
        void createMessage_profileDoesNotExist_throwsNotFoundException() {
            var profileId = UUID.randomUUID();
            var roomId = new ChatRoomId(UUID.randomUUID());

            when(profilesApi.existsById(profileId)).thenReturn(false);

            assertThatThrownBy(() -> service.createMessage(roomId, profileId, "hello"))
                    .isInstanceOf(be.kdg.ip3.archportal.communications.domain.NotFoundException.class)
                    .hasMessageContaining("Profile does not exist");
        }

        @Test
        void createMessage_messageToLong_throwsIllegalArgumentException() {
            var sender = UUID.randomUUID();
            var other = UUID.randomUUID();
            var room = new ChatRoom("t");
            room.addMember(sender);
            room.addMember(other);
            var text = "This message is too long".repeat(21);

            var roomId = room.getId();
            when(profilesApi.existsById(sender)).thenReturn(true);
            when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(room));

            assertThatThrownBy(() -> service.createMessage(roomId, sender, text))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Message is too long");
        }
    }

    @Test
    void onFriendShipCreated_createsChatRoomWithMembersAndSaves() {
        var aId = UUID.randomUUID();
        var bId = UUID.randomUUID();
        var aTag = "alice";
        var bTag = "bob";

        var event = new FriendShipCreatedEvent(aId, aTag, bId, bTag);

        var captor = ArgumentCaptor.forClass(ChatRoom.class);
        service.onFriendShipCreated(event);

        verify(chatRoomRepository).save(captor.capture());
        var room = captor.getValue();
        assertThat(room.getMembers()).containsExactlyInAnyOrder(aId, bId);
        assertThat(room.getTitle()).contains(aTag).contains(bTag);
    }

    @Test
    void createMessage_addsMessageSavesAndPublishesNotifications() {
        var sender = UUID.randomUUID();
        var other = UUID.randomUUID();
        var room = new ChatRoom("t");
        room.addMember(sender);
        room.addMember(other);

        var roomId = room.getId();
        when(profilesApi.existsById(sender)).thenReturn(true);
        when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(room));

        var message = service.createMessage(roomId, sender, "hi there");

        assertThat(message).returns(sender, Message::getSenderId).returns("hi there", Message::getText);
        verify(chatRoomRepository).save(room);
        verify(applicationEventPublisher, times(2)).publishEvent(any(AddNotificationEvent.class));
    }
}
