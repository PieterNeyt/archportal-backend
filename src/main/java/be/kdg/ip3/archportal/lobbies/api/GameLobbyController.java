package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.api.dto.SessionInfo;
import be.kdg.ip3.archportal.lobbies.api.dto.StartSinglePlayerRequest;
import be.kdg.ip3.archportal.lobbies.api.dto.StartSinglePlayerResponse;
import be.kdg.ip3.archportal.lobbies.application.GameLobbyService;
import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/lobbies")
@CrossOrigin(origins = "*")
public class GameLobbyController {

    private final GameLobbyService gameLobbyService;

    public GameLobbyController(GameLobbyService gameLobbyService) {
        this.gameLobbyService = gameLobbyService;
    }

    @PostMapping("/singleplayer/start")
    public ResponseEntity<StartSinglePlayerResponse> startSinglePlayer(
            @Valid @RequestBody StartSinglePlayerRequest request
    ) {

        GameLobby lobby = gameLobbyService.createSinglePlayerLobby(
                new PlayerId(request.playerId()),
                new GameId(request.gameId())
        );

        GameSession session = gameLobbyService.startSession(
                new PlayerId(request.playerId()),
                lobby.getGameLobbyId()
        );

        var response = new StartSinglePlayerResponse(
                lobby.getGameLobbyId().id(),
                session.getGameSessionId().id(),
                session.getLaunchUrl()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionInfo> validateSession(@PathVariable UUID sessionId) {

        GameLobby lobby = gameLobbyService.validateSession(new GameSessionId(sessionId));

        GameSession session = lobby.getSessions().stream()
                .filter(s -> s.getGameSessionId().equals(new GameSessionId(sessionId)))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(
                new SessionInfo(
                        session.getGameSessionId().id(),
                        lobby.getGameLobbyId().id(),
                        session.getPlayerId().id(),
                        lobby.getGameId().id().toString()
                )
        );
    }


}
