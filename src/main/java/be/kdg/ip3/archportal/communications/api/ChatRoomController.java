package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.CreateChatRoomDto;
import be.kdg.ip3.archportal.communications.application.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateChatRoomDto dto, @AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        chatRoomService.createChatRoom(dto.memberIds(), profileId);
        return ResponseEntity.ok().build();
    }
}
