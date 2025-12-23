package be.kdg.ip3.archportal.communications.domain.chatroom;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(ChatRoomId id);
    
    List<ChatRoom> findChatRoomsOfProfileIdWithLastMessage(UUID profileId);

    void deleteById(ChatRoomId chatRoomId);
}
