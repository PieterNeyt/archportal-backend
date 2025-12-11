package be.kdg.ip3.archportal.profiles.shared;

import be.kdg.ip3.archportal.profiles.domain.profile.Profile;

import java.util.UUID;

public record ProfileDto(
        UUID id,
        String firstName,
        String lastName,
        String icon,
        String gamerTag
) {
    public static ProfileDto from(Profile profile) {
        return new ProfileDto(profile.getId().id(), profile.getFirstName(), profile.getLastName(), profile.getIcon(), profile.getGamerTag());
    }
}