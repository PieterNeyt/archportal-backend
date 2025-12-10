package be.kdg.ip3.archportal.profiles.api.dto;

import be.kdg.ip3.archportal.profiles.domain.profile.Profile;

public record ProfileDto(
        String firstName,
        String lastName,
        String icon,
        String gamerTag,
        String email
) {
    public static ProfileDto from(Profile profile) {
        return new ProfileDto(profile.getFirstName(), profile.getLastName(), profile.getIcon(), profile.getGamerTag(),profile.getEmail());
    }
}
