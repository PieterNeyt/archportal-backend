package be.kdg.ip3.archportal.lobbies.domain.partyInvite;

import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record PartyInviteId(UUID id) {
    public PartyInviteId {
        Assert.notNull(id, "id is null");
    }
    public NotFoundException notFound() {
        return new NotFoundException("party invite not found");
    }
}
