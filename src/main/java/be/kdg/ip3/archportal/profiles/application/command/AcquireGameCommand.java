package be.kdg.ip3.archportal.profiles.application.command;

import be.kdg.ip3.archportal.profiles.api.dto.AcquireGameDto;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;

import java.util.List;
import java.util.UUID;

public record AcquireGameCommand(
        ProfileId profileId,
        List<UUID> games
) {
    public static AcquireGameCommand fromDto(AcquireGameDto dto) {
        return new AcquireGameCommand(
                new ProfileId(dto.profileId()),
                dto.games()
        );
    }
}