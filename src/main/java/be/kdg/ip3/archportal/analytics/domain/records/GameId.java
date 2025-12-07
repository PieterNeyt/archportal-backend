package be.kdg.ip3.archportal.analytics.domain.records;

import java.util.UUID;

public record GameId(UUID id) {
    public static GameId create() {
        return new GameId(UUID.randomUUID());
    }
}
