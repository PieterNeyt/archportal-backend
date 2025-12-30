package be.kdg.ip3.archportal.lobbies.domain.lobby;

import be.kdg.ip3.archportal.lobbies.domain.GameId;
import be.kdg.ip3.archportal.lobbies.domain.session.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameLobbyRepository {
    void save(GameLobby lobby);

    Optional<GameLobby> findById(GameLobbyId id);

    Optional<GameLobby> findLobbyBySessionId(GameSessionId sessionId);

    Optional<UUID> findPlayerBySessionId(GameSessionId sessionId);

    List<GameLobby> findAllLobbiesByGameId(GameId gameId);

    boolean isPlayerInLobby(PlayerId playerId);

    Optional<GameLobby> getLobbyFromPLayerID(PlayerId profileId);
    
    void delete(GameLobby lobby);
}
