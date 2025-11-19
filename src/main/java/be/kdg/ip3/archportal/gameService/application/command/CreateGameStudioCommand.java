package be.kdg.ip3.archportal.gameService.application.command;

import be.kdg.ip3.archportal.gameService.api.dto.CreateGameStudioDto;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.gameService.domain.owner.Owner;

import java.util.UUID;

public record CreateGameStudioCommand(
        UUID id,
        UUID ownerId,
        String name,
        String description,
        String IBAN,
        String ownerFirstName,
        String ownerLastName,
        String ownerEmail
) {
    public static CreateGameStudioCommand fromDto(CreateGameStudioDto studioDto) {
        return new CreateGameStudioCommand(
                studioDto.id(),
                studioDto.ownerId(),
                studioDto.name(),
                studioDto.description(),
                studioDto.IBAN(),
                studioDto.ownerFirstName(),
                studioDto.ownerLastName(),
                studioDto.ownerEmail()
        );
    }

    public static CreateGameStudioCommand fromDomain(GameStudio studio, Owner owner) {
        return new CreateGameStudioCommand(
                studio.getId().id(),
                owner.getId().id(),
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail()
        );
    }
}

