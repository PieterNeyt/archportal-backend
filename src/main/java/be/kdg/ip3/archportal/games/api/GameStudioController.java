package be.kdg.ip3.archportal.games.api;

import be.kdg.ip3.archportal.games.api.dto.CreateGameStudioDto;
import be.kdg.ip3.archportal.games.application.GameStudioService;
import be.kdg.ip3.archportal.games.application.command.CreateGameStudioCommand;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/gamestudio")
public class GameStudioController {
    private final GameStudioService gameStudioService;

    public GameStudioController(GameStudioService gameStudioService) {
        this.gameStudioService = gameStudioService;
    }

    @PostMapping()
    public ResponseEntity<CreateGameStudioDto> createGameStudio(@Valid @RequestBody CreateGameStudioDto studioDto,
                                                                @AuthenticationPrincipal Jwt token) {
        var ownerId = new OwnerId(UUID.fromString(token.getSubject()));
        var createStudioCommand = CreateGameStudioCommand.fromDto(studioDto, ownerId);
        var studio = gameStudioService.createGameStudio(createStudioCommand);
        var location = URI.create("/api/gamestudio/" + studio.id().id());

        return ResponseEntity
                .created(location)
                .body(CreateGameStudioDto.fromDomain(studio));
    }


}
