package be.kdg.ip3.archportal.lobbies.api.dto;

import jakarta.validation.constraints.NotEmpty;

public record SendInviteDto(
        @NotEmpty
        String gamerTag
) {
}
