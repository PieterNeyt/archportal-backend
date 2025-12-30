package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.ChatRoomApi;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.lobbies.api.dto.MemberDto;
import be.kdg.ip3.archportal.lobbies.api.dto.PartyInviteDto;
import be.kdg.ip3.archportal.lobbies.api.dto.PartyMembersDto;
import be.kdg.ip3.archportal.lobbies.api.dto.PlayerDto;
import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.GameId;
import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyId;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import be.kdg.ip3.archportal.lobbies.shared.JoinedPartyEvent;
import be.kdg.ip3.archportal.lobbies.shared.LeftPartyEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProfilesApi profilesApi;
    private final ChatRoomApi chatRoomApi;
    private final GamesApi gamesApi;
    private final GameLobbyService gameLobbyService;
    private final ApplicationEventPublisher publisher;

    public PartyService(PartyRepository partyRepository, ProfilesApi profilesApi, ChatRoomApi chatRoomApi, GamesApi gamesApi, GameLobbyService gameLobbyService, ApplicationEventPublisher publisher) {
        this.partyRepository = partyRepository;
        this.profilesApi = profilesApi;
        this.chatRoomApi = chatRoomApi;
        this.gamesApi = gamesApi;
        this.gameLobbyService = gameLobbyService;
        this.publisher = publisher;
    }

    public Party createParty(PlayerId hostId, String title, int maxMembers) {
        if (!profilesApi.existsById(hostId.id()))
            throw hostId.notFound();
        if (partyRepository.existsByPlayerId(hostId))
            throw new IllegalArgumentException("Already in a party");

        var id = chatRoomApi.createChatRoom(hostId.id());
        var party = new Party(hostId, new ChatRoomId(id), title, maxMembers);
        partyRepository.save(party);
        return party;
    }

    public Party findPartyByMemberId(PlayerId memberId) {
        if (!profilesApi.existsById(memberId.id()))
            throw memberId.notFound();

        return partyRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundException("Party not found"));
    }

    @Transactional(readOnly = true)
    public PartyMembersDto findMembers(PlayerId memberId) {
        if (!profilesApi.existsById(memberId.id()))
            throw memberId.notFound();

        var party = partyRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("Party not found"));

        var memberIds = party.getAllMembers().stream().map(PlayerId::id).toList();

        List<MemberDto> members = profilesApi.getBasicProfiles(memberIds).stream()
                .map(p -> MemberDto.from(p, party.getHostId(), party))
                .toList();

        return new PartyMembersDto(members, party.getStartedLobbyId());
    }

    public PartyInvite sendInvite(PlayerId memberId, String gamerTag) {
        if (!profilesApi.existsById(memberId.id()))
            throw memberId.notFound();
        var receiverId = new PlayerId(profilesApi.getProfileFromGamerTag(gamerTag));

        var party = partyRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundException("Party not found"));
        var invite = party.addInvite(memberId, receiverId);
        partyRepository.save(party);
        publisher.publishEvent(new AddNotificationEvent(receiverId.id(), "Party invite", "You have a new party invite.", NotificationType.PARTY_INVITE));
        return invite;
    }

    public List<PlayerDto> getInvitableFriends(PlayerId playerId) {
        if (!profilesApi.existsById(playerId.id()))
            throw playerId.notFound();

        var party = partyRepository.findByMemberId(playerId).orElseThrow(() -> new NotFoundException("Party not found"));
        var friends = profilesApi.getAllFriends(playerId.id());
        var notInParty = friends.stream().filter(f -> !party.getAllMembers().contains(new PlayerId(f.id()))).toList();
        return notInParty.stream().map(p -> party.hasInvite(new PlayerId(p.id()))
                ? PlayerDto.hasInvite(p)
                : PlayerDto.noInvite(p)).toList();
    }

    public List<PartyInviteDto> getPartiesWhereUserIsInvited(PlayerId playerId) {
        if (!profilesApi.existsById(playerId.id()))
            throw playerId.notFound();

        return partyRepository.findPartyHasInvite(playerId).stream()
                .map(p -> {
                    var invite = p.getInvites().stream().filter(i -> i.getReceiverId().equals(playerId))
                            .findFirst().orElseThrow();
                    var senderGamerTag = profilesApi.getProfileGamerTag(invite.getSenderId().id());
                    return PartyInviteDto.from(p, senderGamerTag);
                }).toList();
    }

    public void acceptPartyInvite(PlayerId playerId, PartyId id) {
        if (!profilesApi.existsById(playerId.id()))
            throw playerId.notFound();
        var party = partyRepository.findById(id).orElseThrow(id::notFound);
        party.acceptPartyInvite(playerId);
        partyRepository.save(party);
        publisher.publishEvent(new JoinedPartyEvent(playerId.id(), party.getChatRoomId().id()));
    }

    public void declinePartyInvite(PlayerId playerId, PartyId id) {
        if (!profilesApi.existsById(playerId.id()))
            throw playerId.notFound();
        var party = partyRepository.findById(id).orElseThrow(id::notFound);
        party.declinePartyInvite(playerId);
        partyRepository.save(party);
    }

    public void leaveParty(PlayerId playerId) {
        if (!profilesApi.existsById(playerId.id()))
            throw playerId.notFound();
        var party = partyRepository.findByMemberId(playerId).orElseThrow(() -> new NotFoundException("Party not found"));
        party.leaveParty(playerId);
        if (party.getHostId() == null)
            partyRepository.deleteById(party.getId());
        else partyRepository.save(party);
        publisher.publishEvent(new LeftPartyEvent(party.getChatRoomId().id(), playerId.id()));
    }

    public void kickFromParty(PlayerId playerId, String gamertag) {
        if (!profilesApi.existsById(playerId.id()))
            throw playerId.notFound();
        var memberId = new PlayerId(profilesApi.getProfileFromGamerTag(gamertag));
        var party = partyRepository.findByMemberId(playerId).orElseThrow(() -> new NotFoundException("Party not found"));
        party.checkHost(playerId);
        party.leaveParty(memberId);
        partyRepository.save(party);
        publisher.publishEvent(new LeftPartyEvent(party.getChatRoomId().id(), memberId.id()));
    }

    public List<GlobalGameDto> getEligibleGames(PlayerId playerId) {
        var party = partyRepository.findByMemberId(playerId)
                .orElseThrow(() -> new NotFoundException("Party not found"));

        List<PlayerId> memberIds = party.getAllMembers();
        var partySize = memberIds.size();

        List<UUID> commonGameIds = null;

        for (PlayerId mId : memberIds) {

            List<UUID> memberGames = profilesApi.getLibraryFromPlayer(mId.id());

            if (commonGameIds == null) {
                commonGameIds = new ArrayList<>(memberGames);
            } else {
                commonGameIds.retainAll(memberGames);
            }
        }

        if (commonGameIds == null || commonGameIds.isEmpty()) return List.of();

        List<GlobalGameDto> eligibleGames = gamesApi.getGamesByIds(commonGameIds).stream()
                .filter(game -> game.maxlobbysize() >= partySize)
                .toList();

        if (party.getSelectedGameId() != null) {
            var stillEligible = eligibleGames.stream().anyMatch(g -> g.id().equals(party.getSelectedGameId()));
            if (!stillEligible) {
                party.selectGame(null);
            }
        }
        partyRepository.save(party);
        return eligibleGames;
    }

    public void selectGame(PlayerId playerId, UUID gameId) {
        var party = partyRepository.findByMemberId(playerId).orElseThrow();
        party.checkHost(playerId);
        party.selectGame(gameId);
        partyRepository.save(party);
    }
    public GlobalGameDto getSelectedGame(PlayerId playerId) {
        var party = partyRepository.findByMemberId(playerId)
                .orElseThrow(() -> new NotFoundException("Party not found"));

        if (party.getSelectedGameId() == null)
            return null;

        return gamesApi.getGameById(party.getSelectedGameId());
    }

    public void toggleReady(PlayerId playerId) {
        var party = partyRepository.findByMemberId(playerId).orElseThrow();
        party.toggleReady(playerId);
        partyRepository.save(party);
    }

    public UUID startPartyGame(PlayerId hostId) {
        var party = partyRepository.findByMemberId(hostId).orElseThrow();
        party.checkHost(hostId);

        if (!party.areAllReady()) {
            throw new IllegalStateException("Not everyone is ready yet!");
        }

        if (party.getSelectedGameId() == null) {
            throw new IllegalStateException("No game selected");
        }

        var gameId = new GameId(party.getSelectedGameId());
        var lobby = gameLobbyService.createMultiplayerLobby(hostId, gameId);

        for (PlayerId member : party.getMembers()) {
            gameLobbyService.joinMultiplayerLobby(member, lobby.getGameLobbyId());
        }

        party.startedLobbyId(lobby.getGameLobbyId().id());
        partyRepository.save(party);

        return lobby.getGameLobbyId().id();
    }
}
