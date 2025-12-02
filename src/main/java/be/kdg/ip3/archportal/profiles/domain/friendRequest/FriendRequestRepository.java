package be.kdg.ip3.archportal.profiles.domain.friendRequest;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepository {
    void save(FriendRequest friendRequest);
    Optional<FriendRequest> findById(FriendRequestId id);
}
