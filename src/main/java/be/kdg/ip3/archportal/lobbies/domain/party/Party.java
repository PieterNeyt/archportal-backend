package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
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
    private int maxMembers;
    private static int totalMaxMembers = 10;

    public Party(PartyId id, PlayerId hostId, Set<PlayerId> playerIds, int maxMembers) {
        this.id = id;
        this.hostId = hostId;
        this.playerIds = playerIds;
        this.maxMembers = maxMembers;
    }

    public Party(PlayerId hostId) {
        this(new PartyId(UUID.randomUUID()), hostId, new HashSet<>(), totalMaxMembers);
    }
}
