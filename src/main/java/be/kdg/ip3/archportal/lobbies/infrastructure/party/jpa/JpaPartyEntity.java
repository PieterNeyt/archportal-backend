package be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa;

import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyId;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyMember;
import be.kdg.ip3.archportal.lobbies.infrastructure.partyInvite.JpaPartyInviteEntity;
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

    @Column
    private UUID selectedGameId;

    @Column
    private UUID startedLobbyId;

    @OneToMany(
            mappedBy = "party",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<JpaPartyMemberEntity> members;

    @Column(nullable = false)
    private int maxMembers;

    @Column(nullable = false)
    private UUID chatRoomId;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<JpaPartyInviteEntity> invites;

    protected JpaPartyEntity() {
    }

    public JpaPartyEntity(
            UUID id,
            String title,
            Set<JpaPartyMemberEntity> members,
            int maxMembers,
            UUID chatRoomId,
            Set<JpaPartyInviteEntity> invites,
            UUID selectedGameId,
            UUID startedLobbyId
    ) {
        this.id = id;
        this.title = title;
        this.members = members;
        this.maxMembers = maxMembers;
        this.chatRoomId = chatRoomId;
        setInvites(invites);
        setMembers(members);
        this.selectedGameId = selectedGameId;
        this.startedLobbyId = startedLobbyId;
    }

    public static JpaPartyEntity fromDomain(Party party) {
        Set<JpaPartyMemberEntity> memberEntities = party.getMembers().stream()
                .map(JpaPartyMemberEntity::fromDomain)
                .collect(Collectors.toSet());

        JpaPartyEntity entity = new JpaPartyEntity(
                party.getId().id(),
                party.getTitle(),
                memberEntities,
                party.getMaxMembers(),
                party.getChatRoomId().id(),
                party.getInvites().stream()
                        .map(JpaPartyInviteEntity::fromDomain)
                        .collect(Collectors.toSet()),
                party.getSelectedGameId(),
                party.getStartedLobbyId()
        );

        return entity;
    }

    public Party toDomain() {
        return new Party(
                new PartyId(id),
                title,
                members.stream()
                        .map(JpaPartyMemberEntity::toDomain)
                        .collect(Collectors.toSet()),
                maxMembers,
                new ChatRoomId(chatRoomId),
                invites.stream()
                        .map(JpaPartyInviteEntity::toDomain)
                        .collect(Collectors.toSet()),
                selectedGameId,
                startedLobbyId
        );
    }

    private void setInvites(Set<JpaPartyInviteEntity> invites) {
        this.invites = invites;
        this.invites.forEach(i -> i.setParty(this));
    }

    private void setMembers(Set<JpaPartyMemberEntity> members) {
        this.members = members;
        this.members.forEach(m -> m.setParty(this));
    }
}
