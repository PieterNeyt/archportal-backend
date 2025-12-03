package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaProfileRepository extends JpaRepository<JpaProfileEntity, UUID> {
    @Query("""
            SELECT p FROM JpaProfileEntity p
            WHERE p.id IN (
                           SELECT f FROM JpaProfileEntity prof
                           JOIN prof.friends f
                           WHERE prof.id = :profileId)
            """)
    List<JpaProfileEntity> findAllFriendsOfProfileId(@Param("profileId") UUID profileId);
}
