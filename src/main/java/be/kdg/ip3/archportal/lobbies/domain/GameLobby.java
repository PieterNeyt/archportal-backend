package be.kdg.ip3.archportal.lobbies.domain;

import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.ArrayList;
import java.util.List;

@Getter
@AggregateRoot
public class GameLobby {
    @Identity
    private GameLobbyId gameLobbyId;
    private GameId gameId;
    private int maxPlayers;
    private GameLobbyStatus gameLobbyStatus;
    private List<PlayerId> players;
    private List<GameSession> sessions;

    public GameLobby(GameLobbyId id, GameId gameId, int maxPlayers) {
        this.gameLobbyId = id;
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.gameLobbyStatus = GameLobbyStatus.OPEN;
        this.players = new ArrayList<>();
        this.sessions = new ArrayList<>();

    }

    public GameLobby(GameLobbyId gameLobbyId, GameId gameId, int maxPlayers, GameLobbyStatus gameLobbyStatus, List<PlayerId> players, List<GameSession> sessions) {
        this.gameLobbyId = gameLobbyId;
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.gameLobbyStatus = gameLobbyStatus;
        this.players = new ArrayList<>(players);
        this.sessions = new ArrayList<>(sessions);
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
    public static GameLobby createMultiplayerLobby(GameId gameId, int maxPlayers) {
        return new GameLobby(
                GameLobbyId.create(),
                gameId,
                maxPlayers
        );
    }

    public void addSession(GameSession session) {
        this.sessions.add(session);
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
    
    public void removePlayer(PlayerId playerId) {
        if (!players.contains(playerId))
            throw new IllegalStateException("Player not found");
        players.remove(playerId);
    }
}
