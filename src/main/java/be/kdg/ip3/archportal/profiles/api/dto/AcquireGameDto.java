package be.kdg.ip3.archportal.profiles.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AcquireGameDto(
        @NotNull(message = "Profile ID is required")
        UUID profileId,

        @NotNull(message = "Games list is required")
        List<UUID> games
) {
}