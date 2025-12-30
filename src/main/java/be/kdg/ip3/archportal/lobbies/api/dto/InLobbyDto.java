package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.UUID;

public record InLobbyDto(UUID lobbyId, boolean isPlayerInLobby, UUID gameId) {
}
