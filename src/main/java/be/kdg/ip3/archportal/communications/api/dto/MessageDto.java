package be.kdg.ip3.archportal.communications.api.dto;

import be.kdg.ip3.archportal.communications.domain.chatroom.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDto(boolean isYou, String text, LocalDateTime timestamp) {
    public static MessageDto fromDomain(Message message, UUID profileId) {
        return new MessageDto(
                message.sender().equals(profileId),
                message.text(),
                message.timestamp()
        );
    }
}
