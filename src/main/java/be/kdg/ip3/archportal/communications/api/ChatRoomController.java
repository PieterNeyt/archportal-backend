package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.ChatRoomDto;
import be.kdg.ip3.archportal.communications.api.dto.CreateChatRoomDto;
import be.kdg.ip3.archportal.communications.application.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ChatRoomDto> create(@Valid @RequestBody CreateChatRoomDto dto, @AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        var chatRoom = chatRoomService.createChatRoom(dto.gamerTags(), profileId);
        var location = URI.create("/api/chat-room/" + chatRoom.getId().id());
        return ResponseEntity.created(location).body(ChatRoomDto.fromDomain(chatRoom));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(@AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        var chatRooms = chatRoomService.getChatRooms(profileId).stream().map(ChatRoomDto::fromDomain).toList();
        return ResponseEntity.ok(chatRooms);
    }
}
