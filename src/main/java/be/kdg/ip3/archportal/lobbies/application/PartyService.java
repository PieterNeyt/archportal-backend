package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.communications.shared.ChatRoomApi;
import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.NotFoundException;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.domain.partyInvite.PartyInvite;
import be.kdg.ip3.archportal.profiles.shared.BasicProfileInfo;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
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

    public PartyService(PartyRepository partyRepository, ProfilesApi profilesApi, ChatRoomApi chatRoomApi) {
        this.partyRepository = partyRepository;
        this.profilesApi = profilesApi;
        this.chatRoomApi = chatRoomApi;
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
        return invite;
    }
}
