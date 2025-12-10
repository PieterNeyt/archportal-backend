package be.kdg.ip3.archportal.profiles.infrastructure.friendship.jpa;

import be.kdg.ip3.archportal.profiles.domain.friendship.Friendship;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "friendship", schema = "profileservice",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"profile_a_id", "profile_b_id"})
        })
public class JpaFriendshipEntity {
    @Id
    private UUID id;
    @Column(nullable = false, name = "profile_a_id")
    private UUID profileAId;
    @Column(nullable = false, name = "profile_b_id")
    private UUID profileBId;

    protected JpaFriendshipEntity() {
    }

    public JpaFriendshipEntity(UUID id, UUID profileAId, UUID profileBId) {
        this.id = id;
        this.profileAId = profileAId;
        this.profileBId = profileBId;
    }

    public static JpaFriendshipEntity fromDomain(Friendship friendship) {
        return new JpaFriendshipEntity(
                friendship.getId().id(),
                friendship.getProfileAId().id(),
                friendship.getProfileBId().id()
        );
    }

    public Friendship toDomain() {
        return new Friendship(
                new FriendshipId(id),
                new ProfileId(profileAId),
                new ProfileId(profileBId)
        );
    }
}
