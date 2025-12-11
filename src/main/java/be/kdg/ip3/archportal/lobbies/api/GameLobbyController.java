package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.api.dto.*;
import be.kdg.ip3.archportal.lobbies.application.GameLobbyService;
import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/lobbies")
public class GameLobbyController {

    private final GameLobbyService gameLobbyService;

    public GameLobbyController(GameLobbyService gameLobbyService) {
        this.gameLobbyService = gameLobbyService;
    }

    @PostMapping("/singleplayer/start")
    public ResponseEntity<StartSinglePlayerResponse> startSinglePlayer(
            @Valid @RequestBody StartSinglePlayerRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));

        GameLobby lobby = gameLobbyService.createSinglePlayerLobby(
                playerId,
                new GameId(request.gameId())
        );

        GameSession session = gameLobbyService.startSession(
                playerId,
                lobby.getGameLobbyId()
        );

        var response = new StartSinglePlayerResponse(
                lobby.getGameLobbyId().id(),
                session.getGameSessionId().id(),
                session.getLaunchUrl()
        );

        var location = URI.create("/api/lobbies/sessions/" + session.getGameSessionId().id());

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/multiplayer/prepare")
    public ResponseEntity<UUID> startMultiplayerLobby(
            @Valid @RequestBody StartMultiPlayerLobbyRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject())); // Degene die de lobby heeft aangemaakt


        GameLobby lobby = gameLobbyService.createMultiplayerLobby(
                playerId,
                new GameId(request.gameId())
        );

        var location = URI.create("/api/lobbies/" + lobby.getGameLobbyId().id());

        return ResponseEntity.created(location).body(lobby.getGameLobbyId().id());
    }

    @PatchMapping("/multiplayer/{lobbyid}/join")
    public ResponseEntity<JoinMultiPlayerResponse> joinMultiplayerLobby(
            @PathVariable UUID lobbyid,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject())); // Degene die de lobby joined
        var lobby = gameLobbyService.joinMultiplayerLobby(
                playerId,
                new GameLobbyId(lobbyid)
        );

        var response = new JoinMultiPlayerResponse(
                lobby.getGameLobbyId().id()
        );

        return ResponseEntity.ok(response);

    }


    @GetMapping("/multiplayer/{gameid}/lobbies")
    public ResponseEntity<LobbiesResponse> getAllLobbies(
            @PathVariable UUID gameid
    ) {

        var lobbies = gameLobbyService.getLobbiesByGameId(new GameId(gameid));

        var response = new LobbiesResponse(
                lobbies.stream().map(lobby -> new GameLobbyInfo(
                        lobby.getGameLobbyId().id(),
                        lobby.getMaxPlayers(),
                        lobby.getPlayers().size(),
                        lobby.getGameLobbyStatus().toString()
                )).toList()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/multiplayer/{lobbyid}/info")
    public ResponseEntity<MultiplayerLobbyInfo> getLobbyInfo(
            @PathVariable UUID lobbyid) {
        var lobby = gameLobbyService.getLobbyInfo(new GameLobbyId(lobbyid));
        var playerInfo = gameLobbyService.getBasicPlayerInfo(new GameLobbyId(lobbyid));

        var response = new MultiplayerLobbyInfo(
                lobby.getGameLobbyId().id(),
                playerInfo,
                lobby.getGameLobbyStatus().toString(),
                lobby.getMaxPlayers()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/player/in-lobby")
    public ResponseEntity<InLobbyDto> isPlayerInLobby(@AuthenticationPrincipal Jwt jwt) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));

        var playerInLobby = gameLobbyService.isPlayerInLobby(playerId);
        if (playerInLobby) {
            var lobbyId = gameLobbyService.getLobbyIdFromPlayerId(playerId);
            var inLobbyDto = new InLobbyDto(lobbyId, playerInLobby);
            return ResponseEntity.ok(inLobbyDto);
        }
        var inLobbyDto = new InLobbyDto(null, playerInLobby);
        return ResponseEntity.ok(inLobbyDto);
    }

    @PostMapping("/multiplayer/start")
    public ResponseEntity<StartMultiPlayerResponse> startMultiplayerSession(
            @Valid @RequestBody StartMultiplayerGameRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));

        var mySession = gameLobbyService.startMultipleSessions(playerId, new GameLobbyId(request.lobbyId()));

        var response = new StartMultiPlayerResponse(
                mySession.getGameLobbyId().id(),
                mySession.getGameSessionId().id(),
                mySession.getLaunchUrl()
        );

        var location = URI.create("/api/lobbies/sessions/" + mySession.getGameSessionId().id());

        return ResponseEntity.created(location).body(response);


    }

    @GetMapping("/multiplayer/{lobbyid}/session")
    public ResponseEntity<StartMultiPlayerResponse> getMySession(
            @PathVariable UUID lobbyid,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));
        var session = gameLobbyService.getPlayerSessionInLobby(
                playerId,
                new GameLobbyId(lobbyid)
        );

        return ResponseEntity.ok(new StartMultiPlayerResponse(
                session.getGameLobbyId().id(),
                session.getGameSessionId().id(),
                session.getLaunchUrl()
        ));
    }


    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionInfo> validateSession(@PathVariable UUID sessionId) {

        GameLobby lobby = gameLobbyService.validateSession(new GameSessionId(sessionId));

        GameSession session = gameLobbyService.getSession(new GameSessionId(sessionId));

        return ResponseEntity.ok(
                new SessionInfo(
                        session.getGameSessionId().id(),
                        lobby.getGameLobbyId().id(),
                        session.getPlayerId().id(),
                        lobby.getGameId().id()
                )
        );
    }

    @PatchMapping()
    public ResponseEntity<Void> leaveLobby(@AuthenticationPrincipal Jwt jwt) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject()));
        gameLobbyService.leaveLobby(playerId);
        return ResponseEntity.ok().build();
    }
}
