package be.kdg.ip3.archportal.communications.domain.chatroom;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
}
