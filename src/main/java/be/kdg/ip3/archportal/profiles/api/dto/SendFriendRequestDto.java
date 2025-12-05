package be.kdg.ip3.archportal.profiles.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SendFriendRequestDto(
        @NotBlank
        String gamerTag) {
}
