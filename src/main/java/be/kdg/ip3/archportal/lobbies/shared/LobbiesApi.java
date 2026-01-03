package be.kdg.ip3.archportal.lobbies.shared;

import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
public interface LobbiesApi {
    UUID getPlayerIdBySessionId(UUID sessionId);
    UUID getGameIdBySessionId(UUID sessionId);
    String getGameTitleOfCurrentGameByProfileId(UUID profileId);
}
