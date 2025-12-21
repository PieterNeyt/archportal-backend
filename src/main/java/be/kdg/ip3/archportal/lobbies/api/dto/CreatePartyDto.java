package be.kdg.ip3.archportal.lobbies.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CreatePartyDto(@NotEmpty @Length(max = 100) String title, @Min(2) @Max(10) int maxMembers) {
}
