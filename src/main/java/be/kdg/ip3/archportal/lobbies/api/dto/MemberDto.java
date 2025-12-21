package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.profiles.shared.BasicProfileInfo;

public record MemberDto(String gamerTag, String icon, boolean isLeader, boolean isReady) {
    public static MemberDto from(BasicProfileInfo member, PlayerId hostId) {
        return new MemberDto(member.gamertag(), member.avatarUrl(), member.id().equals(hostId.id()), false);
    }
}
