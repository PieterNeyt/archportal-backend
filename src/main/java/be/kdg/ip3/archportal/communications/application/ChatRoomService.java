package be.kdg.ip3.archportal.communications.application;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.profiles.shared.FriendShipCreatedEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProfilesApi profilesApi;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ProfilesApi profilesApi) {
        this.chatRoomRepository = chatRoomRepository;
        this.profilesApi = profilesApi;
    }

    @ApplicationModuleListener
    public void onFriendShipCreated(FriendShipCreatedEvent event) {
        var chatRoom = new ChatRoom();
        chatRoom.addMember(event.profileAId());
        chatRoom.addMember(event.profileBId());
        chatRoomRepository.save(chatRoom);
    }

    //TODO checken of wel vrienden zijn en checken of wel bestaan
    public void createChatRoom(List<UUID> memberIds, UUID creatorId) {
        var chatRoom = new ChatRoom();
        chatRoom.addMember(creatorId);
        chatRoom.addMembers(memberIds);
        chatRoomRepository.save(chatRoom);
    }
}
