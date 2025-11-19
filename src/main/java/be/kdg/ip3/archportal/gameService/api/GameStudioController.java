package be.kdg.ip3.archportal.gameService.api;

import be.kdg.ip3.archportal.gameService.api.dto.GameStudioDto;
import be.kdg.ip3.archportal.gameService.application.GameStudioService;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
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
    public ResponseEntity<GameStudioDto> createGameStudio(@RequestBody GameStudioDto studioDto) {
        GameStudio studio = gameStudioService.createGameStudio(studioDto);
        URI location = URI.create("/api/gamestudio/" + studio.getId().id());
        return ResponseEntity
                .created(location)
                .body(GameStudioDto.fromDomain(studio));
    }


}
