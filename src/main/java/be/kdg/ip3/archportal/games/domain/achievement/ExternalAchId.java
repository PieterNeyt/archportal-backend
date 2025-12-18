package be.kdg.ip3.archportal.games.domain.achievement;

import org.springframework.util.Assert;

import java.util.UUID;

public record ExternalAchId(UUID id) {

    public ExternalAchId {
        Assert.notNull(id, "id is null");
    }

    public static ExternalAchId create() {
        return new ExternalAchId(UUID.randomUUID());
    }
}
