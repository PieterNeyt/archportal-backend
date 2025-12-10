package be.kdg.ip3.archportal.lobbies.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaGameLobbyRepository extends JpaRepository<JpaGameLobbyEntity, UUID> {

    @Query("""
        SELECT lobby
        FROM JpaGameLobbyEntity lobby
        JOIN lobby.sessions session
        WHERE session.gameSessionId = :sessionId
    """)
    Optional<JpaGameLobbyEntity> findLobbyBySessionId(UUID sessionId);

    @Query("""
        SELECT session.playerId
        FROM JpaGameLobbyEntity lobby
        JOIN lobby.sessions session
        WHERE session.gameSessionId = :sessionId
    """)
    Optional<UUID> findPlayerIdBySessionId(UUID sessionId);

    @Query("""
        SELECT lobby
        FROM JpaGameLobbyEntity lobby
        WHERE lobby.gameId = :gameId
    """)
    List<JpaGameLobbyEntity> findAllLobbiesByGameId(UUID gameId);
}
