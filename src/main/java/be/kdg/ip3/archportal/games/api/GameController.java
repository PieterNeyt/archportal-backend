package be.kdg.ip3.archportal.games.api;

import be.kdg.ip3.archportal.games.api.dto.AchievementDto;
import be.kdg.ip3.archportal.games.api.dto.GameDto;
import be.kdg.ip3.archportal.games.application.GameService;
import be.kdg.ip3.archportal.games.application.command.AchievementCommand;
import be.kdg.ip3.archportal.games.application.command.GameCommand;
import be.kdg.ip3.archportal.games.domain.game.GameId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        var game = gameService.createGameFromHttp(GameCommand.fromDto(gameDto), ownerId);
        var location = URI.create("/api/games/" + game.getId().id());
        return ResponseEntity.created(location).body(GameDto.fromDomain(game));
    }

    @PostMapping("/{gameId}/achievement")
    public ResponseEntity<AchievementDto> addAchievement(@PathVariable("gameId") UUID gameUUId,
            @Valid @RequestBody AchievementDto achievemntDto, @AuthenticationPrincipal Jwt token) {
        var ownerId = new OwnerId(UUID.fromString(token.getSubject()));
        var gameId = new GameId(gameUUId);

        var achievement = gameService.addAchievement(AchievementCommand.fromDto(achievemntDto), ownerId,gameId);
        var location = URI.create("/api/games/"+ gameUUId.toString() +"/achievement/"+ achievement.getId().id());

        return ResponseEntity.created(location).body(AchievementDto.fromDomain(achievement));
    }


    @GetMapping({"/studio"})
    public ResponseEntity<List<GameDto>> getGamesFromStudio(@AuthenticationPrincipal Jwt token) {
        var ownerId = new OwnerId(UUID.fromString(token.getSubject()));
        var games = gameService.getGamesFromStudio(ownerId);
        return ResponseEntity.ok(games.stream()
                .map(GameDto::fromDomain)
                .toList());
    }

    @PutMapping()
    public ResponseEntity<GameDto> updateGame(@RequestBody GameDto gameDto,@AuthenticationPrincipal Jwt token) {
        var ownerId = new OwnerId(UUID.fromString(token.getSubject()));
        var game = gameService.updateGame(GameCommand.fromDto(gameDto),ownerId);
        return ResponseEntity.ok(GameDto.fromDomain(game));
    }
}
