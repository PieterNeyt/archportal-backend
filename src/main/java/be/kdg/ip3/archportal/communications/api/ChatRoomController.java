package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.ChatRoomDto;
import be.kdg.ip3.archportal.communications.api.dto.CreateChatRoomDto;
import be.kdg.ip3.archportal.communications.api.dto.CreateMessageDto;
import be.kdg.ip3.archportal.communications.application.ChatRoomService;
import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoomId;
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
        return ResponseEntity.created(location).body(ChatRoomDto.fromDomain(chatRoom, profileId));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getChatRoomsWithLastMessage(@AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        var chatRooms = chatRoomService.getChatRoomsWithLastMessage(profileId).stream()
                .map(chatRoom -> ChatRoomDto.fromDomain(chatRoom, profileId)).toList();
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        var chatRoom = chatRoomService.getChatRoom(new ChatRoomId(id), profileId);
        return ResponseEntity.ok(ChatRoomDto.fromDomain(chatRoom, profileId));
    }

    @PostMapping("/{chatRoomId}/messages")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody CreateMessageDto dto,
                                            @PathVariable UUID chatRoomId,
                                            @AuthenticationPrincipal Jwt token) {
        var profileId = UUID.fromString(token.getSubject());
        var message = chatRoomService.createMessage(new ChatRoomId(chatRoomId), profileId, dto.text());
        var location = URI.create("/api/chat-room/" + chatRoomId + "/" + message.getId().id());
        return ResponseEntity.created(location).build();
    }
}
