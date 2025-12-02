package be.kdg.ip3.archportal.profiles.api;

import be.kdg.ip3.archportal.profiles.api.dto.SendFriendRequestDto;
import be.kdg.ip3.archportal.profiles.application.FriendRequestService;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/friend-request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping
    public ResponseEntity<Void> sendFriendRequest(SendFriendRequestDto dto, @AuthenticationPrincipal Jwt token) {
        var senderId = new ProfileId(UUID.fromString(token.getSubject()));
        var receiverId = new ProfileId(dto.receiverId());
        var request = friendRequestService.createFriendRequest(senderId, receiverId);

        var location = URI.create("/api/friend-request/" + request.getId());
        return ResponseEntity.created(location).build();
    }
}