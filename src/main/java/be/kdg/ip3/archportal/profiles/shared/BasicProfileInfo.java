package be.kdg.ip3.archportal.profiles.shared;

import be.kdg.ip3.archportal.profiles.domain.profile.Profile;

import java.util.UUID;

public record BasicProfileInfo(
        UUID id,
        String gamertag,
        String avatarUrl,
        UUID activeUsernameColorId
) {
    public static BasicProfileInfo from(Profile profile) {
        return new BasicProfileInfo(
                profile.getId().id(),
                profile.getGamerTag(),
                profile.getIcon(),
                profile.getActiveUsernameColorId()
        );
    }
}
