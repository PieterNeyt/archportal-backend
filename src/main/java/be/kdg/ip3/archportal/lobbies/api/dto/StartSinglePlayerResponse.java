package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.UUID;

public record StartSinglePlayerResponse(
        UUID lobbyId,
        UUID sessionId,
        String launchUrl
) {}
