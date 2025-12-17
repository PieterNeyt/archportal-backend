package be.kdg.ip3.archportal.communications.shared;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;

import java.util.List;
import java.util.UUID;

public record ChatRoomDto(UUID id, String title, List<MessageDto> messages) {
    public static ChatRoomDto fromDomain(ChatRoom chatRoom, UUID profileId) {
        return new ChatRoomDto(chatRoom.getId().id(), chatRoom.getTitle(),
                chatRoom.getMessages().stream().map(mes -> MessageDto.fromDomain(mes, profileId)).toList());
    }
}
