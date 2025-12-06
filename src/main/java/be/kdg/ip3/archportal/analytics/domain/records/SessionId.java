package be.kdg.ip3.archportal.analytics.domain.records;

import java.util.UUID;

public record SessionId(UUID id) {
    public static SessionId create() {
        return new SessionId(UUID.randomUUID());
    }
}
