package be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa;

import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class JpaFriendRequest {
    @Column(nullable = false, name = "sender_id")
    private UUID senderId;

    public JpaFriendRequest() {
    }

    public JpaFriendRequest(UUID senderId) {
        this.senderId = senderId;
    }

    public static JpaFriendRequest from(FriendRequest friendRequest) {
        return new JpaFriendRequest(friendRequest.senderId().id());
    }

    public FriendRequest toDomain() {
        return new FriendRequest(new ProfileId(senderId));
    }
}
