package be.kdg.ip3.archportal.communications.domain.chatroom;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
    List<ChatRoom> findChatRoomsWithoutMessagesOfProfileId(UUID profileId);

    Optional<ChatRoom> findById(ChatRoomId id);

    Optional<ChatRoom>  findByIdWithLastMessage(ChatRoomId chatRoomId);
}
