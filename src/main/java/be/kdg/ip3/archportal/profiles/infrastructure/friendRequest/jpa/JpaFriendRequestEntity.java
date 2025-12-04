package be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa;

import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestId;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestState;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "friend_requests", schema = "profileservice")
public class JpaFriendRequestEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID senderId;
    @Column(nullable = false)
    private UUID receiverId;
    @Enumerated(EnumType.STRING)
    private FriendRequestState state;

    public JpaFriendRequestEntity() {
    }

    public JpaFriendRequestEntity(UUID id, UUID senderId, UUID receiverId, FriendRequestState state) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.state = state;
    }

    public static JpaFriendRequestEntity from(FriendRequest friendRequest) {
        return new JpaFriendRequestEntity(friendRequest.getId().id(), friendRequest.getSenderId().id(),
                friendRequest.getReceiverId().id(), friendRequest.getState());
    }
    
    public FriendRequest toDomain() {
        return new FriendRequest(new FriendRequestId(id), new ProfileId(senderId), new ProfileId(receiverId), state);
    }
}
