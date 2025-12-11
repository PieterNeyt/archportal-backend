package be.kdg.ip3.archportal.communications.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateMessageDto(@NotBlank @Length(max = 500) String text) {
}
