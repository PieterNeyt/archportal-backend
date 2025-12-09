package be.kdg.ip3.archportal.games.api;

import be.kdg.ip3.archportal.games.api.dto.GameDto;
import be.kdg.ip3.archportal.games.application.GameService;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<GameDto> createGame(@Valid @RequestBody GameDto gameDto, @AuthenticationPrincipal Jwt token) {
        var ownerId = new OwnerId(UUID.fromString(token.getSubject()));
        var game = gameService.createGame(gameDto, ownerId);
        var location = URI.create("/api/games/" + game.getId().id());
        return ResponseEntity.created(location).body(GameDto.fromDomain(game));
    }

    @GetMapping({"/studio"})
    public ResponseEntity<List<GameDto>> getGamesFromStudio(@AuthenticationPrincipal Jwt token) {
        var ownerId = new OwnerId(UUID.fromString(token.getSubject()));
        var games = gameService.getGamesFromStudio(ownerId);
        return ResponseEntity.ok(games.stream()
                .map(GameDto::fromDomain)
                .toList());
    }
}
