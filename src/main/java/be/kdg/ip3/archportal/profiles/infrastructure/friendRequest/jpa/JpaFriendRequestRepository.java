package be.kdg.ip3.archportal.profiles.infrastructure.friendRequest.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaFriendRequestRepository extends JpaRepository<JpaFriendRequestEntity, UUID> {
    boolean existsBySenderIdAndReceiverId(UUID senderId, UUID receiverId);
    List<JpaFriendRequestEntity> findAllByReceiverId(UUID receiverId);
}
