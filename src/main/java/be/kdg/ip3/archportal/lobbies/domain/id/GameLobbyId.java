package be.kdg.ip3.archportal.lobbies.domain.id;

import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;

import java.util.UUID;

public record GameLobbyId(UUID id) {
    public GameLobbyId {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }
    public static GameLobbyId create() {
        return new GameLobbyId(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        return new NotFoundException("lobby [" + id + "] not found");
    }

}
