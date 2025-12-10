package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.UUID;

public record GameLobbyFace(
        UUID lobbyId,
        int maxPlayers,
        int currentPlayers,
        String status
) {
}
