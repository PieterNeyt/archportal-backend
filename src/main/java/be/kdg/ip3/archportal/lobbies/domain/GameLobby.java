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

    public GameLobby(GameLobbyId gameLobbyId, GameId gameId, int maxPlayers, GameLobbyStatus gameLobbyStatus, List<PlayerId> players, List<GameSessionId> sessions) {
        this.gameLobbyId = gameLobbyId;
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.gameLobbyStatus = gameLobbyStatus;
        this.players = players;
        this.sessions = sessions;
    }

    public static GameLobby newSinglePlayerLobby(GameId gameId) {
        var lobby = new GameLobby(
                GameLobbyId.create(),
                gameId,
                1
        );
        lobby.closeLobby();
        return lobby;
    }
    public static GameLobby createMultiplayerLobby() {
        //TODO: implement multiplayer lobby creation logic
        return null;
    }

    public void closeLobby() {
        this.gameLobbyStatus = GameLobbyStatus.CLOSED;
    }

    public void addPlayer(PlayerId playerId) {
        if (players.size() >= maxPlayers) {
            throw new IllegalStateException("Lobby is full");
        }
        players.add(playerId);
        if (players.size() == maxPlayers) {
            gameLobbyStatus = GameLobbyStatus.FULL;
        }
    }
}
