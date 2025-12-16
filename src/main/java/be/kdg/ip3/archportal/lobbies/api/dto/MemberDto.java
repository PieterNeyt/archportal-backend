package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.profiles.shared.BasicProfileInfo;

public record MemberDto(String gamerTag, String icon, Boolean isLeader, Boolean isReady) {
    public static MemberDto from(BasicProfileInfo member) {
        return new MemberDto(member.gamertag(), member.avatarUrl(), true, false);
    }
}
