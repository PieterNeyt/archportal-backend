package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.profiles.shared.BasicProfileInfo;

import java.util.UUID;

public record MemberDto(String gamerTag, String icon, boolean isLeader, boolean isReady, UUID activeUsernameColorId) {
    public static MemberDto from(BasicProfileInfo member, PlayerId hostId, Party party) {
        PlayerId playerId = new PlayerId(member.id());
        boolean isReady = party.getMemberReadyStatus().getOrDefault(playerId, false);
        return new MemberDto(member.gamertag(), member.avatarUrl(), member.id().equals(hostId.id()), isReady, member.activeUsernameColorId());
    }
}
