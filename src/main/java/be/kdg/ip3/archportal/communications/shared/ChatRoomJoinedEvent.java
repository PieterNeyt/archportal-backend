package be.kdg.ip3.archportal.communications.shared;

import java.util.UUID;

public record ChatRoomJoinedEvent(UUID profileId, UUID chatRoomId) {
}
