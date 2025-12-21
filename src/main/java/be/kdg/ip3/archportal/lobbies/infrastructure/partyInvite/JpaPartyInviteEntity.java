package be.kdg.ip3.archportal.lobbies.infrastructure.partyInvite;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInviteId;
import be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa.JpaPartyEntity;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "party_invite", schema = "lobbyservice")
public class JpaPartyInviteEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID senderId;
    @Column(nullable = false)
    private UUID receiverId;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private JpaPartyEntity party;

    protected JpaPartyInviteEntity() {
    }

    public JpaPartyInviteEntity(UUID id, UUID senderId, UUID receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public static JpaPartyInviteEntity fromDomain(PartyInvite invite) {
        return new JpaPartyInviteEntity(
                invite.getId().id(),
                invite.getSenderId().id(),
                invite.getReceiverId().id()
        );
    }

    public PartyInvite toDomain() {
        return new PartyInvite(
                new PartyInviteId(id),
                new PlayerId(senderId),
                new PlayerId(receiverId)
        );
    }
}
