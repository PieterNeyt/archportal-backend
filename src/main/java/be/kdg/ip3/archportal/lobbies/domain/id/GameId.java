package be.kdg.ip3.archportal.lobbies.domain.id;

import java.util.UUID;

public record GameId(UUID id) {
    public GameId {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }

    public static GameId create() {
        return new GameId(UUID.randomUUID());
    }
}
