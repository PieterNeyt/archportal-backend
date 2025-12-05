package be.kdg.ip3.archportal.analytics.domain.records;

import java.util.UUID;

public record ProfileId(UUID id) {
    public static GameId create() {
        return new GameId(UUID.randomUUID());
    }

}
