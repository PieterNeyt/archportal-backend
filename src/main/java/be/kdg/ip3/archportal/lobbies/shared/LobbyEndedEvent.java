package be.kdg.ip3.archportal.lobbies.shared;

import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobbyId;

public record LobbyEndedEvent(GameLobbyId lobbyId) {
}
