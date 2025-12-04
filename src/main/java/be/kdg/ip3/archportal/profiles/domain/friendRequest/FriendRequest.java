package be.kdg.ip3.archportal.profiles.domain.friendRequest;

import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
@Getter
public class FriendRequest {
    private final FriendRequestId id;
    private final ProfileId senderId;
    private final ProfileId receiverId;

    public FriendRequest(ProfileId senderId, ProfileId receiverId) {
        this.id = FriendRequestId.create();
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public FriendRequest(FriendRequestId id, ProfileId senderId, ProfileId receiverId) {
        this.id = id;
        this.receiverId = receiverId;
        this.senderId = senderId;
    }
}
