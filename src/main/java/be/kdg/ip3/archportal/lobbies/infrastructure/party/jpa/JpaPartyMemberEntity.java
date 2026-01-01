package be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyMember;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "party_member", schema = "lobbyservice")
@Getter
public class JpaPartyMemberEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID playerId;

    @Column(nullable = false)
    private boolean isHost;

    @Column(nullable = false)
    private boolean isReady;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private JpaPartyEntity party;

    protected JpaPartyMemberEntity() {
    }

    private JpaPartyMemberEntity(UUID playerId, boolean isHost, boolean isReady) {
        this.playerId = playerId;
        this.isHost = isHost;
        this.isReady = isReady;
    }


    public static JpaPartyMemberEntity fromDomain(PartyMember member) {
        return new JpaPartyMemberEntity(
                member.getPlayerId().id(),
                member.isHost(),
                member.isReady()
        );
    }

    public PartyMember toDomain() {
        return new PartyMember(
                new PlayerId(playerId),
                isHost,
                isReady
        );
    }

    void setParty(JpaPartyEntity party) {
        this.party = party;
    }
}
