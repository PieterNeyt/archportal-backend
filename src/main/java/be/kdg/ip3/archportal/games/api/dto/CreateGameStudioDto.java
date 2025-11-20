package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.application.command.CreateGameStudioCommand;

import java.util.UUID;

public record CreateGameStudioDto(
        UUID id,
        UUID ownerId,
        String name,
        String description,
        String IBAN,
        String ownerFirstName,
        String ownerLastName,
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

