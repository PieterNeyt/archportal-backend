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
    private FriendRequestState state;

    public FriendRequest(ProfileId senderId, ProfileId receiverId) {
        this.id = FriendRequestId.create();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.state = FriendRequestState.PENDING;
    }

    public FriendRequest(FriendRequestId id, ProfileId senderId, ProfileId receiverId, FriendRequestState state) {
        this.id = id;
        this.state = state;
        this.receiverId = receiverId;
        this.senderId = senderId;
    }
}
