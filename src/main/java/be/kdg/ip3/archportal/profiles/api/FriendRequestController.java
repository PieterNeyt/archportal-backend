package be.kdg.ip3.archportal.profiles.api;

import be.kdg.ip3.archportal.profiles.api.dto.ProfileDto;
import be.kdg.ip3.archportal.profiles.api.dto.SendFriendRequestDto;
import be.kdg.ip3.archportal.profiles.application.FriendRequestService;
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
@RequestMapping("/api/friends")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;
    private final ProfileService profileService;

    public FriendRequestController(FriendRequestService friendRequestService, ProfileService profileService) {
        this.friendRequestService = friendRequestService;
        this.profileService = profileService;
    }

    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@Valid @RequestBody SendFriendRequestDto dto, @AuthenticationPrincipal Jwt token) {
        var senderId = new ProfileId(UUID.fromString(token.getSubject()));
        var request = friendRequestService.createFriendRequest(senderId, dto.gamerTag());

        var location = URI.create("/api/friends/request/" + request.getId().id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<ProfileDto>> getFriends(@AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var friends = profileService.getAllFriends(profileId).stream().map(ProfileDto::from).toList();
        return ResponseEntity.ok(friends);
    }
}