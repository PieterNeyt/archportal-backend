package be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa;

import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyId;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "party", schema = "lobbyservice")
public class JpaPartyEntity {
    @Id
    private UUID id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false)
    private UUID hostId;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "party_member", schema = "lobbyservice",
            joinColumns = @JoinColumn(name = "party_id"))
    private Set<UUID> members;
    @Column(nullable = false)
    private int maxMembers;

    protected JpaPartyEntity() {
    }

    public JpaPartyEntity(UUID id, String title, UUID hostId, Set<UUID> members, int maxMembers) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.members = members;
        this.maxMembers = maxMembers;
    }

    public static JpaPartyEntity fromDomain(Party party) {
        return new JpaPartyEntity(
                party.getId().id(),
                party.getTitle(),
                party.getHostId().id(),
                party.getMembers().stream().map(PlayerId::id).collect(Collectors.toSet()),
                party.getMaxMembers()
        );
    }

    public Party toDomain() {
        return new Party(
                new PartyId(id),
                title,
                new PlayerId(hostId),
                members.stream().map(PlayerId::new).collect(Collectors.toSet()),
                maxMembers
        );
    }
}
