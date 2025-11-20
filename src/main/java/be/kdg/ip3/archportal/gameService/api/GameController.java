package be.kdg.ip3.archportal.gameService.api;

import be.kdg.ip3.archportal.gameService.api.dto.GameDto;
import be.kdg.ip3.archportal.gameService.application.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<GameDto> createGame(@Valid @RequestBody GameDto gameDto) {
        var game = gameService.createGame(gameDto);
        var location = URI.create("/api/games/" + game.getId().id());
        return ResponseEntity.created(location).body(GameDto.fromDomain(game));
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<GameDto>> getAllGames() {
        var games = gameService.findAll();
        return ResponseEntity.ok(games.stream().map(GameDto::fromDomain).toList());
    }
}
