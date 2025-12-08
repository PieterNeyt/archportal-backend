package be.kdg.ip3.archportal.communications.api.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record CreateChatRoomDto(@NotEmpty List<UUID> memberIds) {
}
