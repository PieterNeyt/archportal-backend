package be.kdg.ip3.archportal.lobbies.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreatePartyDto(@NotBlank String title, @Min(1) @Max(10) int maxMembers) {
}
