package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.List;

public record LobbiesResponse(
        List<GameLobbyFace> lobbies
) {
}
