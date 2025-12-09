package be.kdg.ip3.archportal.communications.infrastructure.chatroom;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa.JpaChatRoomEntity;
import be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa.JpaChatRoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class DbChatRoomRepository implements ChatRoomRepository {
    private final JpaChatRoomRepository chatRoomRepository;

    public DbChatRoomRepository(JpaChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public List<ChatRoom> findChatRoomsOfProfileId(UUID profileId) {
        return chatRoomRepository.findChatRoomsOfProfileId(profileId).stream().map(JpaChatRoomEntity::toDomain).toList();
    }

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(JpaChatRoomEntity.fromDomain(chatRoom));
    }
}
