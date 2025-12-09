package be.kdg.ip3.archportal.communications.api.dto;

import be.kdg.ip3.archportal.communications.domain.chatroom.ChatRoom;

public record ChatRoomDto(String title) {
    public static ChatRoomDto fromDomain(ChatRoom chatRoom) {
        return new ChatRoomDto(chatRoom.getTitle());
    }
}
