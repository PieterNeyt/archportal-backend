package be.kdg.ip3.archportal.lobbies.domain;

import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface GameLobbyRepository {
    void save(GameLobby lobby);
    void saveSession(GameSession session);
    Optional<GameLobby> findById(GameLobbyId id);
    Optional<GameSession> findById(GameSessionId id);



}
