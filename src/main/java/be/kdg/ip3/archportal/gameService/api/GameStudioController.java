package be.kdg.ip3.archportal.gameService.api;

import be.kdg.ip3.archportal.gameService.application.GameStudioService;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(GameStudioDto.fromDomain(studio));
    }


}
