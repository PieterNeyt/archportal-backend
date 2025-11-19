package be.kdg.ip3.archportal.gameService.api;

import be.kdg.ip3.archportal.gameService.api.dto.GameDto;
import be.kdg.ip3.archportal.gameService.application.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
        var location = URI.create("/api/gamestudio/" + game.getId().id());
        return ResponseEntity.created(location).body(GameDto.fromDomain(game));
    }
}
