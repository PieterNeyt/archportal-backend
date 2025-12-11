package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.lobbies.api.dto.PlayerLobbyInfo;
import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import be.kdg.ip3.archportal.lobbies.domain.id.*;
import be.kdg.ip3.archportal.lobbies.shared.LobbiesApi;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GameLobbyService implements LobbiesApi {

    private final GameLobbyRepository gameLobbies;
    private final GamesApi gamesApi;
    private final ProfilesApi profileApi;

    public GameLobbyService(GameLobbyRepository gameLobbies, GamesApi gamesApi, ProfilesApi profileApi) {
        this.gameLobbies = gameLobbies;
        this.gamesApi = gamesApi;
        this.profileApi = profileApi;
    }

    @Override
    public UUID getPlayerIdBySessionId(UUID sessionId) {
        var sessionIdObj = new GameSessionId(sessionId);

        GameLobby lobby = validateSession(sessionIdObj);
        return gameLobbies.findPlayerBySessionId(sessionIdObj)
                .orElseThrow(() -> new IllegalArgumentException("No player found for sessionId: " + sessionId));
    }

    @Override
    public UUID getGameIdBySessionId(UUID sessionId) {
        var sessionIdObj = new GameSessionId(sessionId);

        GameLobby lobby = validateSession(sessionIdObj);
        return lobby.getGameId().id();
    }


    public GameLobby createSinglePlayerLobby(PlayerId playerId, GameId gameId) {
        var lobby = GameLobby.newSinglePlayerLobby(gameId);

        lobby.addPlayer(playerId);
        gameLobbies.save(lobby);
        return lobby;
    }

    public GameLobby createMultiplayerLobby(PlayerId playerId, GameId gameId) {
        var maxPlayers = gamesApi.getMaxPlayersForGame(gameId.id());
        var lobby = GameLobby.createMultiplayerLobby(gameId, maxPlayers);

        lobby.addPlayer(playerId);
        gameLobbies.save(lobby);
        return lobby;

    }

    public GameLobby joinMultiplayerLobby(PlayerId playerId, GameLobbyId lobbyId) {

        var lobby = gameLobbies.findById(lobbyId)
                .orElseThrow(lobbyId::notFound);

        lobby.addPlayer(playerId);

        gameLobbies.save(lobby);

        return lobby;
    }

    public List<GameLobby> getLobbiesByGameId(GameId gameId) {
        return gameLobbies.findAllLobbiesByGameId(gameId);
    }

    public GameLobby getLobbyInfo(GameLobbyId lobbyId) {
        return gameLobbies.findById(lobbyId)
                .orElseThrow(lobbyId::notFound);
    }

    public GameSession getPlayerSessionInLobby(PlayerId playerId, GameLobbyId lobbyId) {
        var lobby = gameLobbies.findById(lobbyId)
                .orElseThrow(lobbyId::notFound);

        return lobby.getSessions().stream()
                .filter(s -> s.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No session found for playerId: " + playerId + " in lobbyId: " + lobbyId));
    }

    public List<PlayerLobbyInfo> getBasicPlayerInfo(GameLobbyId lobbyId) {
        var lobby = gameLobbies.findById(lobbyId)
                .orElseThrow(lobbyId::notFound);

        var playerIds = lobby.getPlayers();
        var response = profileApi.getBasicProfiles(playerIds.stream().map(PlayerId::id).toList());


        return response.stream()
                .map(p -> new PlayerLobbyInfo(
                        p.Gamertag(),
                        p.avatarUrl()
                )).toList();
    }


    public GameSession startSession(PlayerId playerId, GameLobbyId lobbyId) {

        var lobby = gameLobbies.findById(lobbyId)
                .orElseThrow(lobbyId::notFound);

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

    public GameSession startMultipleSessions(PlayerId ownerId, GameLobbyId lobbyId) {

        var lobby = gameLobbies.findById(lobbyId)
                .orElseThrow(lobbyId::notFound);

        lobby.requirePlayerIsInLobby(ownerId);

        String baseLaunchUrl = gamesApi.getGameUrl(lobby.getGameId().id());

        List<GameSession> sessions = lobby.getPlayers().stream()
                .map(playerId -> GameSession.create(
                        lobby.getGameLobbyId(),
                        playerId,
                        baseLaunchUrl
                )).toList();

        sessions.forEach(lobby::addSession);

        lobby.closeLobby();

        gameLobbies.save(lobby);
        return sessions.stream()
                .filter(s -> s.getPlayerId().equals(ownerId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Owner session not found"));
    }

    public GameLobby validateSession(GameSessionId sessionId) {
        return gameLobbies.findLobbyBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    public GameSession getSession(GameSessionId sessionId) {

        GameLobby lobby = validateSession(sessionId);

        return lobby.getSessions().stream()
                .filter(s -> s.getGameSessionId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }


    public boolean isPlayerInLobby(PlayerId playerId) {
        return this.gameLobbies.isPlayerInLobby(playerId);
    }

    public UUID getLobbyIdFromPlayerId(PlayerId playerId) {
        return this.gameLobbies.getLobbyIdFromPLayerID(playerId)
                .orElseThrow(playerId::notFound);
    }

    public void leaveLobby(PlayerId playerId) {
        var lobby = gameLobbies.getLobbyFromPLayerID(playerId).orElseThrow(playerId::notFound);

        lobby.removePlayer(playerId);
        if (lobby.getPlayers().isEmpty()) {
            gameLobbies.delete(lobby);
            return;
        }
        gameLobbies.save(lobby);
    }
}
