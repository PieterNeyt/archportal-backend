package be.kdg.ip3.archportal.profiles.domain.friendship;

import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@AggregateRoot
public class Friendship {
    @Id
    private final FriendshipId id;
    private final ProfileId profileAId;
    private final ProfileId profileBId;

    public Friendship(FriendshipId id, ProfileId profileAId, ProfileId profileBId) {
        this.id = id;
        this.profileAId = profileAId;
        this.profileBId = profileBId;
    }
    
    public Friendship(ProfileId profileAId, ProfileId profileBId) {
        this.id = new FriendshipId(UUID.randomUUID());
        this.profileAId = profileAId;
        this.profileBId = profileBId;
    }
}
