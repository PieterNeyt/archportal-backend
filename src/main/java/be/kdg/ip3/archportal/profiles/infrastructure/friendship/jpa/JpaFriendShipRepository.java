package be.kdg.ip3.archportal.profiles.infrastructure.friendship.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaFriendShipRepository extends JpaRepository<JpaFriendshipEntity, UUID> {
    @Query("""
                select f from JpaFriendshipEntity f
                where (f.profileAId = :id1 and f.profileBId = :id2)
                   or (f.profileAId = :id2 and f.profileBId = :id1)
            """)
    Optional<JpaFriendshipEntity> findBetween(UUID id1, UUID id2);
    
    @Query("""
        select case when count(f) > 0 then true else false end
        from JpaFriendshipEntity f
        where (f.profileAId = :a and f.profileBId = :b)
           or (f.profileAId = :b and f.profileBId = :a)
    """)
    boolean existsBetween(UUID a, UUID b);
}
