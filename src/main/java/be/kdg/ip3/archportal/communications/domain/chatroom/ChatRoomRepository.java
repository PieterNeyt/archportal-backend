package be.kdg.ip3.archportal.communications.domain.chatroom;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
    List<ChatRoom> findChatRoomsOfProfileId(UUID profileId);
}
