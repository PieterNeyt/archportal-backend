package be.kdg.ip3.archportal.gameService.api;

import be.kdg.ip3.archportal.gameService.api.dto.CreateGameStudioDto;
import be.kdg.ip3.archportal.gameService.application.GameStudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/gamestudio")
public class GameStudioController {
    private final GameStudioService gameStudioService;

    public GameStudioController(GameStudioService gameStudioService) {
        this.gameStudioService = gameStudioService;
    }

    @PostMapping()
    public ResponseEntity<CreateGameStudioDto> createGameStudio(@RequestBody CreateGameStudioDto studioDto) {
        var studio = gameStudioService.createGameStudio(studioDto);
        var location = URI.create("/api/gamestudio/" + studio.getId().id());

        return ResponseEntity
                .created(location)
                .body(CreateGameStudioDto.fromDomain(studio));
    }


}
