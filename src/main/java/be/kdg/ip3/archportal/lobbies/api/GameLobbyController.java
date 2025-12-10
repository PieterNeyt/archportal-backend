package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.api.dto.*;
import be.kdg.ip3.archportal.lobbies.application.GameLobbyService;
import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;
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

    @PostMapping("/multiplayer/start")
    public ResponseEntity<StartMultiPlayerResponse> startMultiplayerLobby(
            @Valid @RequestBody StartMultiPlayerRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        var playerId = new PlayerId(UUID.fromString(jwt.getSubject())); // Degene die de lobby heeft aangemaakt


        GameLobby lobby = gameLobbyService.createMultiplayerLobby(
                playerId,
                new GameId(request.gameId())
        );


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


}
