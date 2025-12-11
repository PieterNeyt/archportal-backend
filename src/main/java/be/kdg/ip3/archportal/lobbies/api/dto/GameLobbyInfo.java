package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.UUID;

public record GameLobbyInfo(
        UUID lobbyId,
        int maxPlayers,
        int currentPlayers,
        String status
) {
}
