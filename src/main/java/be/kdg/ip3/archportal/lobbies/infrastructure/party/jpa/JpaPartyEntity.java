package be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa;

import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "party", schema = "lobbyservice")
public class JpaPartyEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID hostId;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "party_member", schema = "lobbyservice",
            joinColumns = @JoinColumn(name = "party_id"))
    private Set<UUID> playerIds;
    @Column(nullable = false)
    private int maxMembers;

    protected JpaPartyEntity() {
    }

    public JpaPartyEntity(UUID id, UUID hostId, Set<UUID> playerIds, int maxMembers) {
        this.id = id;
        this.hostId = hostId;
        this.playerIds = playerIds;
        this.maxMembers = maxMembers;
    }

    public static JpaPartyEntity fromDomain(Party party) {
        return new JpaPartyEntity(
                party.getId().id(),
                party.getHostId().id(),
                party.getPlayerIds().stream().map(PlayerId::id).collect(Collectors.toSet()),
                party.getMaxMembers()
        );
    }
}
