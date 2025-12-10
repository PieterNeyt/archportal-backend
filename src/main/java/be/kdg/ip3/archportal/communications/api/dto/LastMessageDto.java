package be.kdg.ip3.archportal.communications.api.dto;

import java.time.LocalDateTime;

public record LastMessageDto(String text, LocalDateTime timestamp) {
}
