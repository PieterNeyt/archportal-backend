package be.kdg.ip3.archportal.communications.api.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateChatRoomDto(@NotEmpty List<String> gamerTags) {
}
