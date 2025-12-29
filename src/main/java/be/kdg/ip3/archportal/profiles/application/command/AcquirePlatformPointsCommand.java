package be.kdg.ip3.archportal.profiles.application.command;

import be.kdg.ip3.archportal.profiles.api.dto.AcquireGameDto;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformPointsEvent;

import java.util.List;
import java.util.UUID;

public record AcquirePlatformPointsCommand(
        ProfileId profileId,
        int platformPoints
) {
    public static AcquirePlatformPointsCommand fromDto(GrantPlatformPointsEvent dto) {
        return new AcquirePlatformPointsCommand(
                new ProfileId(dto.profileId()),
                dto.points()
        );
    }
}