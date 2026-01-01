package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobby;

import java.util.List;
import java.util.UUID;

public record MultiplayerLobbyInfo(
        UUID lobbyId,
        List<PlayerLobbyInfo> players,
        String status,
        int maxPlayers,
        UUID gameId
) {
    public static MultiplayerLobbyInfo from(GameLobby lobby, List<PlayerLobbyInfo> playerInfo) {
        return new MultiplayerLobbyInfo(
                lobby.getGameLobbyId().id(),
                playerInfo,
                lobby.getGameLobbyStatus().toString(),
                lobby.getMaxPlayers(),
                lobby.getGameId().id()
        );
    }
}
