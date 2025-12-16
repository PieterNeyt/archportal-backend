package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AggregateRoot
public class Party {
    @Identity
    private PartyId id;
    private PlayerId hostId;
    private Set<PlayerId> playerIds;

    public Party(PartyId id, PlayerId hostId, Set<PlayerId> playerIds) {
        this.id = id;
        this.hostId = hostId;
        this.playerIds = playerIds;
    }

    public Party(PlayerId hostId) {
        this(new PartyId(UUID.randomUUID()), hostId, new HashSet<>());
    }
}
