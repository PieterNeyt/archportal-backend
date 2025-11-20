package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.application.command.CreateGameStudioCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateGameStudioDto(
        UUID id,
        UUID ownerId,
        @NotNull
        String name,
        @NotNull
        String description,
        @NotNull
        String IBAN,
        @NotNull
        String ownerFirstName,
        @NotNull
        String ownerLastName,
        @NotNull
        String ownerEmail
) {
    public static CreateGameStudioDto fromDomain(CreateGameStudioCommand studioCommand) {
        return new CreateGameStudioDto(
                studioCommand.id(),
                studioCommand.ownerId(),
                studioCommand.name(),
                studioCommand.description(),
                studioCommand.IBAN(),
                studioCommand.ownerFirstName(),
                studioCommand.ownerLastName(),
                studioCommand.ownerEmail()

        );
    }
}

