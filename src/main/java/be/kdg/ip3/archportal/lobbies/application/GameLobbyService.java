package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.id.*;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GameLobbyService implements LobbiesApi {

    private final GameLobbyRepository gameLobbies;
    private final GamesApi gamesApi;

    public GameLobbyService(GameLobbyRepository gameLobbies, GamesApi gamesApi) {
        this.gameLobbies = gameLobbies;
        this.gamesApi = gamesApi;
    }

    @Override
    public UUID getPlayerIdBySessionId(UUID sessionId) {
        GameSessionId sessionIdObj = new GameSessionId(sessionId);

        GameLobby lobby = validateSession(sessionIdObj);
        return gameLobbies.findPlayerBySessionId(sessionIdObj)
                .orElseThrow(() -> new IllegalArgumentException("No player found for sessionId: " + sessionId));
    }


    public GameLobby createSinglePlayerLobby(PlayerId playerId, GameId gameId) {
        var lobby = GameLobby.newSinglePlayerLobby(gameId);

        lobby.addPlayer(playerId);
        gameLobbies.save(lobby);
        return lobby;
    }

    public GameSession startSession(PlayerId playerId, GameLobbyId lobbyId) {

        GameLobby lobby = gameLobbies.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("No lobby found for id: " + lobbyId));

        String baseLaunchUrl = gamesApi.getGameUrl(lobby.getGameId().id());

        var session = GameSession.create(
                lobby.getGameLobbyId(),
                playerId,
                baseLaunchUrl
        );

        lobby.addSession(session);

        gameLobbies.save(lobby);

        return session;
    }

    public GameLobby validateSession(GameSessionId sessionId) {
        return gameLobbies.findLobbyBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("No lobby found for sessionId: " + sessionId));
    }

    public GameSession getSession(GameSessionId sessionId) {

        GameLobby lobby = validateSession(sessionId);

        return lobby.getSessions().stream()
                .filter(s -> s.getGameSessionId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }






}
