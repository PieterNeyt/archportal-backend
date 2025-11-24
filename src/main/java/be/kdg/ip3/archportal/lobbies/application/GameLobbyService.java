package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameLobbyService {
    private final GameLobbyRepository gameLobbies;

    public GameLobbyService(GameLobbyRepository gameLobbies) {
        this.gameLobbies = gameLobbies;
    }

    public GameLobby createSinglePlayerLobby(PlayerId playerId, GameId gameId) {
        var lobby = GameLobby.newSinglePlayerLobby(gameId);

        lobby.addPlayer(playerId);
        gameLobbies.save(lobby);
        return lobby;
    }

//    public GameSession startSession(PlayerId playerId, GameLobbyId lobbyId) {
//        GameSession session = GameSession.startNew(playerId, lobbyId);
//        gameLobbies.save(session);
//
//        // link session to lobby
//        GameLobby lobby = gameLobbies.findById(lobbyId).orElseThrow();
//        lobby.ad(session.getGameSessionId());
//        gameLobbies.save(lobby);
//
//        return session;
//    }


}
