package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.profiles.shared.ProfileDto;

public record PlayerDto(
        boolean hasInvite,
        String icon,
        String gamerTag) {
    public static PlayerDto hasInvite(ProfileDto profile) {
        return new PlayerDto(true, profile.icon(), profile.gamerTag());
    }

    public static PlayerDto noInvite(ProfileDto profile) {
        return new PlayerDto(false, profile.icon(), profile.gamerTag());
    }
}
