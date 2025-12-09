package be.kdg.ip3.archportal.communications.application;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.profiles.shared.FriendShipCreatedEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfileDto;
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

    public List<ChatRoom> getChatRoomsWithoutMessages(UUID profileId) {
        return chatRoomRepository.findChatRoomsWithoutMessagesOfProfileId(profileId);
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
}
