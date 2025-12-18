package be.kdg.ip3.archportal.games.domain.achievement;

import org.springframework.util.Assert;

import java.util.UUID;

public record ExternalAchId(String id) {

    public ExternalAchId {
        Assert.notNull(id, "id is null");
    }
}
