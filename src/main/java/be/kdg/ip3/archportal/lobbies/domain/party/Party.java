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
    private String title;
    private PlayerId hostId;
    private Set<PlayerId> members;
    private int maxMembers;
    private static int totalMaxMembers = 10;

    public Party(PartyId id, String title, PlayerId hostId, Set<PlayerId> members, int maxMembers) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.members = members;
        this.maxMembers = maxMembers;
    }

    public Party(PlayerId hostId) {
        this(new PartyId(UUID.randomUUID()),"Pro squad", hostId, new HashSet<>(), totalMaxMembers);
    }
}
