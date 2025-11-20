package be.kdg.ip3.archportal.profileService.api;

import be.kdg.ip3.archportal.gameService.api.dto.CreateGameStudioDto;
import be.kdg.ip3.archportal.gameService.application.command.CreateGameStudioCommand;
import be.kdg.ip3.archportal.profileService.api.dto.AcquireGameDto;
import be.kdg.ip3.archportal.profileService.api.dto.CreateProfileDto;
import be.kdg.ip3.archportal.profileService.application.ProfileService;
import be.kdg.ip3.archportal.profileService.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profileService.application.command.CreateProfileCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<CreateProfileDto> createProfile(@Valid @RequestBody CreateProfileDto dto) {
        var createProfileCommand = CreateProfileCommand.fromDto(dto);
        var profile = profileService.createProfile(createProfileCommand);
        var location = URI.create("/api/profile/" + profile.id());

        return ResponseEntity
                .created(location)
                .body(CreateProfileDto.fromDomain(profile));
    }

    @PutMapping("/acquire")
    public ResponseEntity<Void> acquireGame(@Valid @RequestBody AcquireGameDto dto) {
        var acquireGameCommand = AcquireGameCommand.fromDto(dto);
        profileService.acquireGames(acquireGameCommand);
        return ResponseEntity.ok().build();
    }

}
