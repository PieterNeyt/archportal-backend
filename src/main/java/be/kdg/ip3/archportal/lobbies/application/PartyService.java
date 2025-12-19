package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.ChatRoomApi;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.lobbies.api.dto.PartyInviteDto;
import be.kdg.ip3.archportal.lobbies.api.dto.PlayerDto;
import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import be.kdg.ip3.archportal.profiles.shared.BasicProfileInfo;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProfilesApi profilesApi;
    private final ChatRoomApi chatRoomApi;
    private final ApplicationEventPublisher publisher;

    public PartyService(PartyRepository partyRepository, ProfilesApi profilesApi, ChatRoomApi chatRoomApi, ApplicationEventPublisher publisher) {
        this.partyRepository = partyRepository;
        this.profilesApi = profilesApi;
        this.chatRoomApi = chatRoomApi;
        this.publisher = publisher;
    }

    public Party createParty(PlayerId hostId) {
        if (!profilesApi.existsById(hostId.id()))
            throw hostId.notFound();
        if (partyRepository.existsByPlayerId(hostId))
            throw new IllegalArgumentException("Already in a party");

        var id = chatRoomApi.createChatRoom(hostId.id());
        var party = new Party(hostId, new ChatRoomId(id));
        partyRepository.save(party);
        return party;
    }

    public Party findPartyByMemberId(PlayerId memberId) {
        if (!profilesApi.existsById(memberId.id()))
            throw memberId.notFound();

        return partyRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundException("Party not found"));
    }

    public List<BasicProfileInfo> findMembers(PlayerId memberId) {
        if (!profilesApi.existsById(memberId.id()))
            throw memberId.notFound();

        var party = partyRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundException("Party not found"));
        var memberIds = Stream.concat(
                party.getMembers().stream().map(PlayerId::id),
                Stream.of(party.getHostId().id())
        ).toList();
        return profilesApi.getBasicProfiles(memberIds);
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
        var notInParty = friends.stream().filter(f -> !party.getMembers().contains(new PlayerId(f.id()))).toList();
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
}
