package be.kdg.ip3.archportal.lobbies.domain;

import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface GameLobbyRepository {
    void save(GameLobby lobby);
    GameLobby findById(GameLobbyId id);



}
