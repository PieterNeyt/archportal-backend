package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.application.command.GameStudioCommand;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
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
    public static GameStudioDto fromCommand(GameStudioCommand studioCommand) {
        return new GameStudioDto(
                studioCommand.id().id(),
                studioCommand.ownerId().id(),
                studioCommand.name(),
                studioCommand.description(),
                studioCommand.IBAN()

        );
    }
    public static GameStudioDto fromDomain(GameStudio studio) {
        return new GameStudioDto(
                studio.getId().id(),
                studio.getOwnerId().id(),
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN()
        );
    }
}

