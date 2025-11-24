package be.kdg.ip3.archportal.lobbies.domain;

import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.List;

@Getter
@AggregateRoot
public class GameLobby {
    private GameLobbyId gameLobbyId;
    private GameId gameId;
    private int maxPlayers;
    private GameLobbyStatus gameLobbyStatus;
    private List<PlayerId> players;
    private List<GameSessionId> sessions;

    public GameLobby(GameLobbyId id, GameId gameId, int maxPlayers) {
        this.gameLobbyId = id;
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.gameLobbyStatus = GameLobbyStatus.OPEN;
    }
}
