package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.UUID;

public record StartMultiPlayerLobbyRequest(
        UUID gameId,
        int maxPlayers
) {
}
