package be.kdg.ip3.archportal.lobbies.infrastructure;

import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import org.springframework.stereotype.Repository;

@Repository
public class DbLobbyRepository implements GameLobbyRepository {
    @Override
    public void save(GameLobby lobby) {

    }

    @Override
    public GameLobby findById(GameLobbyId id) {
        return null;
    }
}
