package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.List;
import java.util.UUID;

public record MultiplayerLobbyInfo(
        UUID lobbyId,
        List<PlayerLobbyInfo> players,
        String status,
        int maxPlayers
) {
}
