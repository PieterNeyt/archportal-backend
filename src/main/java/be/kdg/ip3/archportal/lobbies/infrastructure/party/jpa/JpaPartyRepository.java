package be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPartyRepository extends JpaRepository<JpaPartyEntity, UUID> {
    @Query("""
                SELECT DISTINCT p
                FROM JpaPartyEntity p
                LEFT JOIN p.members m
                WHERE p.hostId = :playerId
                   OR m = :playerId
            """)
    Optional<JpaPartyEntity> findByMemberId(UUID playerId);

    @Query("""
                SELECT COUNT(p) > 0
                FROM JpaPartyEntity p
                LEFT JOIN p.members m
                WHERE p.hostId = :playerId
                   OR m = :playerId
            """)
    boolean existsByPlayerId(@Param("playerId") UUID playerId);

    List<JpaPartyEntity> findByInvites_ReceiverId(UUID receiverId);
    Optional<JpaPartyEntity> findByStartedLobbyId(UUID startedLobbyId);
}
