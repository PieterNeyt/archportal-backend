package be.kdg.ip3.archportal.lobbies.domain.id;

import java.util.UUID;

public record GameSessionId(UUID id) {
    public GameSessionId {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }

    public static GameSessionId create() {
        return new GameSessionId(UUID.randomUUID());
    }
}
