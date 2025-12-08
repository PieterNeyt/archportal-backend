package be.kdg.ip3.archportal.communications.infrastructure.chatroom;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomRepository;
import be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa.JpaChatRoomEntity;
import be.kdg.ip3.archportal.communications.infrastructure.chatroom.jpa.JpaChatRoomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbChatRoomRepository implements ChatRoomRepository {
    private final JpaChatRoomRepository chatRoomRepository;

    public DbChatRoomRepository(JpaChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(JpaChatRoomEntity.fromDomain(chatRoom));
    }
}
