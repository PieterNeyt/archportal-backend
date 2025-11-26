package be.kdg.ip3.archportal.lobbies.domain.id;

import java.util.UUID;

public record PlayerId(
        UUID id
) {

    public PlayerId {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }

    public static PlayerId create() {
        return new PlayerId(UUID.randomUUID());
    }

}
