package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.application.command.CreateGameStudioCommand;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GameStudioDto(
        UUID id,
        UUID ownerId,
        @NotNull
        String name,
        @NotNull
        String description,
        @NotNull
        String IBAN
) {
    public static GameStudioDto fromDomain(CreateGameStudioCommand studioCommand) {
        return new GameStudioDto(
                studioCommand.id().id(),
                studioCommand.ownerId().id(),
                studioCommand.name(),
                studioCommand.description(),
                studioCommand.IBAN()

        );
    }
}

