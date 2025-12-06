package be.kdg.ip3.archportal.profiles.domain.friendRequest;

import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record FriendRequest(ProfileId senderId) {
}
