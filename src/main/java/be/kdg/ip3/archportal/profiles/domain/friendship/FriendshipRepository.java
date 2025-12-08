package be.kdg.ip3.archportal.profiles.domain.friendship;

import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository {
    void save(Friendship friendship);

    void delete(Friendship friendship);

    Optional<Friendship> findBetween(ProfileId profileAId, ProfileId profileBId);
    
    boolean existsBetween(ProfileId profileAId, ProfileId profileBId);
}
