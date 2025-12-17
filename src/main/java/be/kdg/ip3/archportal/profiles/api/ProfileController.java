package be.kdg.ip3.archportal.profiles.api;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.api.dto.FriendRequestDto;
import be.kdg.ip3.archportal.profiles.api.dto.LibraryGameDto;
import be.kdg.ip3.archportal.profiles.api.dto.ProfileDto;
import be.kdg.ip3.archportal.profiles.application.ProfileService;
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

    @GetMapping({"", "/"})
    public ResponseEntity<ProfileDto> syncUser(@AuthenticationPrincipal Jwt token) {
        var profile = profileService.syncUser(token);
        return ResponseEntity.ok(ProfileDto.from(profile));
    }

    @GetMapping("/library")
    public ResponseEntity<List<LibraryGameDto>> getLibrary(@AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var library = profileService.getLibrary(profileId);
        return ResponseEntity.ok(library);
    }

    @PostMapping("/friend-request")
    public ResponseEntity<Void> sendFriendRequest(@Valid @RequestBody FriendRequestDto dto, @AuthenticationPrincipal Jwt token) {
        var senderId = new ProfileId(UUID.fromString(token.getSubject()));
        profileService.createFriendRequest(senderId, dto.gamerTag());

        var location = URI.create("/api/profile/" + dto.gamerTag() + "/friend-request");
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/friends")
    public ResponseEntity<List<ProfileDto>> getFriends(@AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var friends = profileService.getAllFriends(profileId).stream().map(ProfileDto::from).toList();
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friend-requests/incoming")
    public ResponseEntity<List<ProfileDto>> getProfilesIncomingRequests(@AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var profiles = profileService.findAllProfilesIncomingRequests(profileId).stream().map(ProfileDto::from).toList();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/friend-requests/outgoing")
    public ResponseEntity<List<ProfileDto>> getProfilesOutgoingRequests(@AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var profiles = profileService.findAllProfilesOutgoingRequests(profileId).stream().map(ProfileDto::from).toList();
        return ResponseEntity.ok(profiles);
    }

    @PutMapping("/friend-request/accept")
    public ResponseEntity<Void> acceptFriendRequest(@Valid @RequestBody FriendRequestDto dto, @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        profileService.acceptFriendRequest(profileId, dto.gamerTag());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/friend-request/decline")
    public ResponseEntity<Void> declineFriendRequest(@Valid @RequestBody FriendRequestDto dto, @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        profileService.declineFriendRequest(profileId, dto.gamerTag());
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/friend-request/cancel")
    public ResponseEntity<Void> cancelFriendRequest(@Valid @RequestBody FriendRequestDto dto, @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        profileService.cancelFriendRequest(profileId, dto.gamerTag());
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/friends")
    public ResponseEntity<Void> deleteFriend(@Valid @RequestBody FriendRequestDto dto,  @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        profileService.removeFriend(profileId, dto.gamerTag());
        return ResponseEntity.noContent().build();
    }
}
