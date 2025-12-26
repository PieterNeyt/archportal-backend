package be.kdg.ip3.archportal.profiles.api.dto;

import be.kdg.ip3.archportal.profiles.domain.profile.Profile;

import java.util.Set;
import java.util.UUID;

public record ProfileDto(
        String firstName,
        String lastName,
        String icon,
        String gamerTag,
        String email,
        Set<UUID> platformBenefits
) {
    public static ProfileDto from(Profile profile) {
        return new ProfileDto(
                profile.getFirstName(),
                profile.getLastName(),
                profile.getIcon(),
                profile.getGamerTag(),
                profile.getEmail(),
                profile.getPlatformBenefits()
        );
    }
}