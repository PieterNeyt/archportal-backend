package be.kdg.ip3.archportal.lobbies.domain.lobby;

import be.kdg.ip3.archportal.lobbies.domain.GameId;
import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.session.GameSession;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AggregateRoot
public class GameLobby {
    @Identity
    private final GameLobbyId gameLobbyId;
    private final GameId gameId;
    private final int maxPlayers;
    private GameLobbyStatus gameLobbyStatus;
    private final List<PlayerId> players;
    private final List<GameSession> sessions;

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

    public Optional<GameSession> removePlayer(PlayerId playerId) {
        if (!players.contains(playerId))
            throw new NotFoundException("Player not found");
        players.remove(playerId);

        var Optionalsession = getGameSession(playerId);
        if (Optionalsession.isPresent()) {
            var session = Optionalsession.get();
            session.endSession();
            return Optional.of(session);
        }

        return Optional.empty();
    }

    public void requirePlayerIsInLobby(PlayerId playerId) {
        if (!players.contains(playerId)) {
            throw new IllegalStateException("Only owner can start the lobby");
        }
    }

    public GameSession endPlayerSession(PlayerId playerId) {
        var Optionalsession = getGameSession(playerId);

        if (Optionalsession.isEmpty())
            throw new NotFoundException("Player session not found for player " + playerId);

        var session = Optionalsession.get();
        session.endSession();
        sessions.remove(session);
        return session;
    }

    public Optional<GameSession> getGameSession(PlayerId playerId) {
        return sessions.stream()
                .filter(s -> s.getPlayerId().equals(playerId))
                .findFirst();

    }
}
