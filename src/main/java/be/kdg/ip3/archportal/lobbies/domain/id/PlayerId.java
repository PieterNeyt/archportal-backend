package be.kdg.ip3.archportal.lobbies.domain.id;


import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;

import java.util.UUID;

public record PlayerId(
        UUID id
) {

    public PlayerId {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }

    public NotFoundException notFound() {
        return new NotFoundException("Player [" + id + "] not found");
    }


    public static PlayerId create() {
        return new PlayerId(UUID.randomUUID());
    }

}

