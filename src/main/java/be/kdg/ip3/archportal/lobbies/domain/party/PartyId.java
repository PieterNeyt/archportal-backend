package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record PartyId(UUID id) {
    public PartyId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Party [" + id + "] not found");
    }
}
