package be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa;

import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public JpaFriendRequestEntity() {
    }

    public JpaFriendRequestEntity(UUID id, UUID senderId, UUID receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public static JpaFriendRequestEntity from(FriendRequest friendRequest) {
        return new JpaFriendRequestEntity(friendRequest.getId().id(), friendRequest.getSenderId().id(),
                friendRequest.getReceiverId().id());
    }
    
    public FriendRequest toDomain() {
        return new FriendRequest(new FriendRequestId(id), new ProfileId(senderId), new ProfileId(receiverId));
    }
}
