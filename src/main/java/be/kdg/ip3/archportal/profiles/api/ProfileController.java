package be.kdg.ip3.archportal.profiles.api;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.api.dto.AcquireGameDto;
import be.kdg.ip3.archportal.profiles.api.dto.CreateProfileDto;
import be.kdg.ip3.archportal.profiles.api.dto.ProfileDto;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    //TODO dit kan gebruikt worden voor updateProfile
//    @PostMapping
//    public ResponseEntity<CreateProfileDto> createProfile(@Valid @RequestBody CreateProfileDto dto) {
//        var createProfileCommand = CreateProfileCommand.fromDto(dto);
//        var profile = profileService.createProfile(createProfileCommand);
//        var location = URI.create("/api/profile/" + profile.id());
//
//        return ResponseEntity
//                .created(location)
//                .body(CreateProfileDto.fromDomain(profile));
//    }

    @GetMapping({"", "/"})
    public ResponseEntity<ProfileDto> syncUser(@AuthenticationPrincipal Jwt token) {
        var profile = profileService.syncUser(token);
        return ResponseEntity.ok(ProfileDto.from(profile));
    }

    @PutMapping("/acquire")
    public ResponseEntity<Void> acquireGame(@Valid @RequestBody AcquireGameDto dto) {
        var acquireGameCommand = AcquireGameCommand.fromDto(dto);
        profileService.acquireGames(acquireGameCommand);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{profileId}/library")
    public ResponseEntity<List<GlobalGameDto>> getLibrary(@PathVariable UUID profileId) {
        var library = profileService.getLibrary(new ProfileId(profileId));
        return ResponseEntity.ok(library);
    }


}
