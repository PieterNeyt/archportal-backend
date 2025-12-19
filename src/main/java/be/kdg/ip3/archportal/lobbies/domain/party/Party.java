package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

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
    private ChatRoomId chatRoomId;
    private Set<PartyInvite> invites;

    public Party(PartyId id, String title, PlayerId hostId, Set<PlayerId> members, int maxMembers, ChatRoomId chatRoomId, Set<PartyInvite> invites) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.members = members;
        this.maxMembers = maxMembers;
        this.chatRoomId = chatRoomId;
        this.invites = invites;
    }

    public Party(PlayerId hostId, ChatRoomId chatRoomId) {
        this(new PartyId(UUID.randomUUID()), "Pro squad", hostId, new HashSet<>(), totalMaxMembers, chatRoomId, new HashSet<>());
    }

    private void checkMember(PlayerId memberId) {
        if (members.contains(memberId))
            throw memberId.notFound();
    }

    public PartyInvite addInvite(PlayerId senderId, PlayerId receiverId) {
        checkMember(receiverId);
        var invite = new PartyInvite(senderId, receiverId);
        if (invites.add(invite))
            return invite;
        throw new IllegalArgumentException("This user already has an invitation");
    }

    public boolean hasInvite(PlayerId playerId) {
        return invites.stream().map(PartyInvite::getReceiverId).toList().contains(playerId);
    }

    public void acceptPartyInvite(PlayerId playerId) {
        var invite = invites.stream().filter(i -> i.getReceiverId().equals(playerId)).findFirst()
                .orElseThrow(() -> new AccessDeniedException("You have no party invite"));
        invites.remove(invite);
        members.add(playerId);
    }
    
    public void declinePartyInvite(PlayerId playerId) {
        var invite = invites.stream().filter(i -> i.getReceiverId().equals(playerId)).findFirst()
                .orElseThrow(() -> new AccessDeniedException("You have no party invite"));
        invites.remove(invite);
    }
}
