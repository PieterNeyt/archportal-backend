package be.kdg.ip3.archportal.communications.application;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.communications.domain.chatroom.Message;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.lobbies.shared.CreatePartyEvent;
import be.kdg.ip3.archportal.profiles.shared.FriendShipCreatedEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfileDto;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProfilesApi profilesApi;
    private final ApplicationEventPublisher eventPublisher;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ProfilesApi profilesApi, ApplicationEventPublisher eventPublisher) {
        this.chatRoomRepository = chatRoomRepository;
        this.profilesApi = profilesApi;
        this.eventPublisher = eventPublisher;
    }

    @ApplicationModuleListener
    public void onFriendShipCreated(FriendShipCreatedEvent event) {
        var chatRoom = new ChatRoom(event.profileAGamertag() + ", " + event.profileBGamertag());
        chatRoom.addMember(event.profileAId());
        chatRoom.addMember(event.profileBId());
        chatRoomRepository.save(chatRoom);
    }

    @ApplicationModuleListener
    public void onPartyCreated(CreatePartyEvent event) {
        var chatRoom = new ChatRoom("Party chat");
        chatRoom.addMember(event.hostId());
        chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getChatRoomsWithLastMessage(UUID profileId) {
        var rooms = chatRoomRepository.findChatRoomsOfProfileIdWithLastMessage(profileId);
        rooms.sort(Comparator.comparing(
                room -> room.getMessages().isEmpty()
                        ? null
                        : room.getMessages()
                        .stream()
                        .map(Message::getTimestamp)
                        .max(Comparator.naturalOrder())
                        .orElse(null),
                Comparator.nullsLast(Comparator.reverseOrder())
        ));
        return rooms;
    }

    public ChatRoom createChatRoom(List<String> gamerTags, UUID creatorId) {
        if (!profilesApi.existsById(creatorId))
            throw new NotFoundException("Creator profile does not exist: " + creatorId);

        var profiles = profilesApi.getProfilesFromGamerTags(gamerTags);
        var foundTags = profiles.stream().map(ProfileDto::gamerTag).toList();
        ChatRoom.validateProfiles(gamerTags, foundTags);

        var friends = profilesApi.getFriendIdsWithCreator(creatorId, profiles.stream().map(ProfileDto::id).toList());
        ChatRoom.validateFriends(profiles.stream().map(ProfileDto::id).toList(), friends);

        var chatRoom = new ChatRoom();
        chatRoom.addMember(creatorId);
        chatRoom.addMembers(profiles.stream().map(ProfileDto::id).toList());
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public ChatRoom getChatRoom(ChatRoomId id, UUID profileId) {
        if (!profilesApi.existsById(profileId))
            throw new NotFoundException("Profile does not exist: " + profileId);

        var chatRoom = chatRoomRepository.findById(id).orElseThrow(id::notFound);
        chatRoom.validateMember(profileId);

        return chatRoom;
    }

    public Message createMessage(ChatRoomId id, UUID profileId, String text) {
        if (!profilesApi.existsById(profileId))
            throw new NotFoundException("Profile does not exist: " + profileId);
        var chatRoom = chatRoomRepository.findById(id).orElseThrow(id::notFound);
        chatRoom.validateMember(profileId);
        var message = new Message(profileId, text);
        chatRoom.addMessage(message);
        chatRoomRepository.save(chatRoom);

        chatRoom.getMembers().forEach(member -> eventPublisher.publishEvent(new AddNotificationEvent(member, "New message from " + chatRoom.getTitle(), text, NotificationType.CHAT)));
        return message;
    }
}
