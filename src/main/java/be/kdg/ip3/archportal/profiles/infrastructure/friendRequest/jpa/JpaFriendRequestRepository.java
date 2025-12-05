package be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa;

import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaFriendRequestRepository extends JpaRepository<JpaFriendRequestEntity, UUID> {
    boolean existsBySenderIdAndReceiverIdAndState(UUID senderId, UUID receiverId,  FriendRequestState state);
}
