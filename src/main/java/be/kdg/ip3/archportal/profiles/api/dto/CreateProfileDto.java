package be.kdg.ip3.archportal.profiles.api.dto;

import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;
public record CreateProfileDto(
        UUID id,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Gamer tag is required")
        String gamerTag,

        String icon
) {
    public static CreateProfileDto fromDomain(CreateProfileCommand command) {
        return new CreateProfileDto(
                command.id(),
                command.firstName(),
                command.lastName(),
                command.gamerTag(),
                command.icon()
        );
    }
}