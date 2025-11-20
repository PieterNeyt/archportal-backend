package be.kdg.ip3.archportal.profileService.application.command;

import be.kdg.ip3.archportal.profileService.api.dto.AcquireGameDto;

import java.util.List;
import java.util.UUID;

public record AcquireGameCommand(
        UUID profileId,
        List<UUID> games
) {
    public static AcquireGameCommand fromDto(AcquireGameDto dto) {
        return new AcquireGameCommand(
                dto.profileId(),
                dto.games()
        );
    }
}