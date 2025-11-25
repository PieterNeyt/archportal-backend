package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.UUID;

public record SessionInfo(
        UUID sessionId,
        UUID gameLobbyId,
        UUID playerId,
        String gameId
) {}