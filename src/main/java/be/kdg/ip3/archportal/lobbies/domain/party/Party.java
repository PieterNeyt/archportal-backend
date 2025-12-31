package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@AggregateRoot
public class Party {
    @Identity
    private final PartyId id;
    private String title;
    private int maxMembers;
    private final ChatRoomId chatRoomId;
    private final Set<PartyInvite> invites;
    private UUID selectedGameId;
    private final static int TOTAL_MAX_MEMBERS = 10;
    private UUID startedLobbyId;
    private final Set<PartyMember> members;

    public Party(PartyId id, String title, Set<PartyMember> members, int maxMembers, ChatRoomId chatRoomId, Set<PartyInvite> invites, UUID selectedGameId, UUID startedLobbyId) {
        this.id = id;
        this.title = title;
        this.members = members;
        setMaxMembers(maxMembers);
        this.chatRoomId = chatRoomId;
        this.invites = invites;
        this.selectedGameId = selectedGameId;
        this.startedLobbyId = startedLobbyId;
    }


    public Party(PlayerId hostId, ChatRoomId chatRoomId, String title, int maxMembers) {
        this(
                new PartyId(UUID.randomUUID()),
                title,
                new HashSet<>(Set.of(new PartyMember(hostId, true, false))),
                maxMembers,
                chatRoomId,
                new HashSet<>(),
                null,
                null
        );
    }
    public boolean isReady(PlayerId playerId) {
        return members.stream()
                .filter(m -> m.getPlayerId().equals(playerId))
                .map(PartyMember::isReady)
                .findFirst()
                .orElse(false);
    }

    public PartyMember getHost() {
        return members.stream()
                .filter(PartyMember::isHost)
                .findFirst()
                .orElse(null);
    }

    public Set<PlayerId> getAllMemberIds() {
        return members.stream().map(PartyMember::getPlayerId).collect(Collectors.toSet());
    }

    private PartyMember getMember(PlayerId playerId) {
        return members.stream()
                .filter(m -> m.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Player is not in this party"));
    }


    private void checkMember(PlayerId playerId) {
        if (members.stream().anyMatch(m -> m.getPlayerId().equals(playerId)))
            throw playerId.notFound();
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

        members.add(new PartyMember(playerId));
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
        return members.stream()
                .map(PartyMember::getPlayerId)
                .toList();
    }


    public void setMaxMembers(int maxMembers) {
        if (maxMembers > TOTAL_MAX_MEMBERS)
            throw new IllegalArgumentException("The maximum amount of members is too high");
        this.maxMembers = maxMembers;
    }

    public void leaveParty(PlayerId playerId) {
        PartyMember leavingMember = getMember(playerId);
        members.remove(leavingMember);

        if (leavingMember.isHost() && !members.isEmpty()) {
            PartyMember newHost = members.iterator().next();
            newHost.makeHost();
        }
    }


    private boolean isHostOrMember(PlayerId playerId) {
        return members.stream().noneMatch(m -> m.getPlayerId().equals(playerId));
    }


    public void checkHost(PlayerId playerId) {
        if (!getHost().getPlayerId().equals(playerId))
            throw new AccessDeniedException("You are not the party leader");
    }

    public void selectGame(UUID gameId) {
        this.selectedGameId=gameId;
    }
    public void toggleReady(PlayerId playerId) {
        getMember(playerId).toggleReady();
    }


    public boolean areAllReady() {
        return members.stream().allMatch(PartyMember::isReady);
    }


    public void startedLobbyId(UUID lobbyId) {
        this.startedLobbyId = lobbyId;
    }

    public void startGameValidation(PlayerId hostId) {
        checkHost(hostId);

        if (!areAllReady())
            throw new IllegalStateException("Not everyone is ready yet");

        if (selectedGameId == null)
            throw new IllegalStateException("No game selected");

        if (startedLobbyId != null)
            throw new IllegalStateException("Party game already started");
    }


}
