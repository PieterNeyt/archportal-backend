package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.application.command.CreateGameStudioCommand;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateGameStudioDto(
        UUID id,
        UUID ownerId,
        @NotNull
        String name,
        @NotNull
        String description,
        @NotNull
        String IBAN
) {
    public static CreateGameStudioDto fromDomain(CreateGameStudioCommand studioCommand) {
        return new CreateGameStudioDto(
                studioCommand.id().id(),
                studioCommand.ownerId().id(),
                studioCommand.name(),
                studioCommand.description(),
                studioCommand.IBAN()

        );
    }
}

