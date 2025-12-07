package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaProfileRepository extends JpaRepository<JpaProfileEntity, UUID> {
    Optional<JpaProfileEntity> findByGamerTag(String gamerTag);

    List<JpaProfileEntity> findByIdIn(List<UUID> profileIds);

    List<JpaProfileEntity> findByIncomingRequests_SenderId(UUID senderId);

    @Query("""
                select p from JpaProfileEntity p
                where p.id in (
                    select case
                        when f.profileAId = :profileId then f.profileBId
                        else f.profileAId
                    end
                    from JpaFriendshipEntity f
                    where f.profileAId = :profileId
                       or f.profileBId = :profileId
                )
            """)
    List<JpaProfileEntity> findAllFriendsOf(UUID profileId);
}
