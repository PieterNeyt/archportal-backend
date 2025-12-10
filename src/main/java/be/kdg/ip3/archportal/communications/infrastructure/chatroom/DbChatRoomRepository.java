package be.kdg.ip3.archportal.communications.infrastructure.chatroom;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa.JpaChatRoomEntity;
import be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa.JpaChatRoomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DbChatRoomRepository implements ChatRoomRepository {
    private final JpaChatRoomRepository chatRoomRepository;

    public DbChatRoomRepository(JpaChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public Optional<ChatRoom> findById(ChatRoomId id) {
        return chatRoomRepository.findById(id.id())
                .map(JpaChatRoomEntity::toDomain);
    }

    @Override
    public Optional<ChatRoom> findByIdWithLastMessage(ChatRoomId chatRoomId) {
        return this.chatRoomRepository.findByIdWithLastMessage(chatRoomId.id())
                .map(JpaChatRoomEntity::toDomain);
    }

    @Override
    public List<ChatRoom> findChatRoomsOfProfileIdWithLastMessage(UUID profileId) {
        return chatRoomRepository.findChatRoomsOfProfileIdWithLastMessage(profileId).stream().map(JpaChatRoomEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(JpaChatRoomEntity.fromDomain(chatRoom));
    }
}
