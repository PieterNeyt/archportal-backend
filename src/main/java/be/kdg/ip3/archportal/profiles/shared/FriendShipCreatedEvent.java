package be.kdg.ip3.archportal.profiles.shared;

import java.util.UUID;

public record FriendShipCreatedEvent(UUID profileAId, String profileAGamertag, UUID profileBId, String profileBGamertag) {
}
