package be.kdg.ip3.archportal.communications.shared;

import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
public interface ChatRoomApi {
    UUID createChatRoom(UUID hostId, String title);
}
