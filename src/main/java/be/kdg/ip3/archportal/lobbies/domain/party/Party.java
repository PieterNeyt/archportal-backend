package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@AggregateRoot
public class Party {
    @Identity
    private final PartyId id;
    private String title;
    private PlayerId hostId;
    private final Set<PlayerId> members;
    private int maxMembers;
    private final ChatRoomId chatRoomId;
    private final Set<PartyInvite> invites;
    private UUID selectedGameId;
    private final static int TOTAL_MAX_MEMBERS = 10;

    public Party(PartyId id, String title, PlayerId hostId, Set<PlayerId> members, int maxMembers, ChatRoomId chatRoomId, Set<PartyInvite> invites,UUID selectedGameId) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.members = members;
        setMaxMembers(maxMembers);
        this.chatRoomId = chatRoomId;
        this.invites = invites;
        this.selectedGameId = selectedGameId;
    }

    public Party(PlayerId hostId, ChatRoomId chatRoomId, String title, int maxMembers) {
        this(new PartyId(UUID.randomUUID()), title, hostId, new HashSet<>(), maxMembers, chatRoomId, new HashSet<>(),null);
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

    private void addMember(PlayerId playerId) {
        if (members.size() >= maxMembers)
            throw new IllegalStateException("Party is already full");
        members.add(playerId);
    }

    public void acceptPartyInvite(PlayerId playerId) {
        var invite = invites.stream().filter(i -> i.getReceiverId().equals(playerId)).findFirst()
                .orElseThrow(() -> new AccessDeniedException("You have no party invite"));
        invites.remove(invite);
        addMember(playerId);
    }

    public void declinePartyInvite(PlayerId playerId) {
        var invite = invites.stream().filter(i -> i.getReceiverId().equals(playerId)).findFirst()
                .orElseThrow(() -> new AccessDeniedException("You have no party invite"));
        invites.remove(invite);
    }

    public List<PlayerId> getAllMembers() {
        return Stream.concat(members.stream(), Stream.of(hostId)).toList();
    }

    public void setMaxMembers(int maxMembers) {
        if (maxMembers > TOTAL_MAX_MEMBERS)
            throw new IllegalArgumentException("The maximum amount of members is too high");
        this.maxMembers = maxMembers;
    }

    public void leaveParty(PlayerId playerId) {
        if (!isMemberOrHost(playerId))
            return;


        if (playerId.equals(hostId)) {
            assignNewHostOrDisband();
        } else {
            members.remove(playerId);
        }
    }
    
    private void assignNewHostOrDisband() {
        if (members.isEmpty()) {
            hostId = null;
            return;
        }

        var newHost = members.iterator().next();
        members.remove(newHost);
        hostId = newHost;
    }

    private boolean isMemberOrHost(PlayerId playerId) {
        return playerId.equals(hostId) || members.contains(playerId);
    }

    public void checkHost(PlayerId playerId) {
        if (!hostId.equals(playerId)) throw new AccessDeniedException("You are not the party leader");
    }
    public void selectGame(UUID gameId) {
        this.selectedGameId=gameId;
    }
}
