package be.kdg.ip3.archportal.lobbies.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record ChatRoomId(UUID id) {
    public ChatRoomId {
        Assert.notNull(id, "id is null");
    }
}