package be.kdg.ip3.archportal.profiles.api;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.api.dto.AcquireGameDto;
import be.kdg.ip3.archportal.profiles.api.dto.CreateProfileDto;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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
    @GetMapping("/{profileId}/library")
    public ResponseEntity<List<GlobalGameDto>> getLibrary(@PathVariable UUID profileId) {
        var library = profileService.getLibrary(profileId);
        return ResponseEntity.ok(library);
    }


}
