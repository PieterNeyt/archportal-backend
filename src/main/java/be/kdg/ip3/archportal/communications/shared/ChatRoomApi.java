package be.kdg.ip3.archportal.communications.shared;

import java.util.UUID;

public interface ChatRoomApi {
    ChatRoomDto findById(UUID chatRoomId, UUID playerId);
}
