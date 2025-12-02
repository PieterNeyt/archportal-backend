package be.kdg.ip3.archportal.profiles.infrastructure.friendRequest;

import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestId;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestRepository;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestState;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa.JpaFriendRequestEntity;
import be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa.JpaFriendRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DbFriendRequestRepository implements FriendRequestRepository {
    private final JpaFriendRequestRepository jpaFriendRequestRepository;

    public DbFriendRequestRepository(JpaFriendRequestRepository jpaFriendRequestRepository) {
        this.jpaFriendRequestRepository = jpaFriendRequestRepository;
    }

    @Override
    public void save(FriendRequest friendRequest) {
        jpaFriendRequestRepository.save(JpaFriendRequestEntity.from(friendRequest));
    }

    @Override
    public Optional<FriendRequest> findById(FriendRequestId id) {
        return jpaFriendRequestRepository.findById(id.id()).map(JpaFriendRequestEntity::toDomain);
    }

    @Override
    public boolean existsPending(ProfileId senderId, ProfileId receiverId) {
        return jpaFriendRequestRepository.existsBySenderIdAndReceiverIdAndState(senderId.id(), receiverId.id(), FriendRequestState.PENDING);
    }
}
