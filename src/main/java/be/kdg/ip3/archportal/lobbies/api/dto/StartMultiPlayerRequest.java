package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.List;
import java.util.UUID;

public record StartMultiPlayerRequest(
        List<UUID> playerIds,
        UUID gameId,
        int maxPlayers
) {
}
