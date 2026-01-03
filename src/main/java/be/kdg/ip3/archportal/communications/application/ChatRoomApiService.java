package be.kdg.ip3.archportal.communications.application;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.communications.shared.ChatRoomApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ChatRoomApiService implements ChatRoomApi {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomApiService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }
    
    @Override
    public UUID createChatRoom(UUID hostId, String title) {
        var chatRoom = new ChatRoom(title);
        chatRoom.addMember(hostId);
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId().id();
    }
}
