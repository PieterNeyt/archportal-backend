package be.kdg.ip3.archportal.games.application.command;

import be.kdg.ip3.archportal.games.api.dto.CreateGameStudioDto;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;

public record CreateGameStudioCommand(
        GameStudioId id,
        OwnerId ownerId,
        String name,
        String description,
        String IBAN,
        String ownerFirstName,
        String ownerLastName,
        String ownerEmail
) {
    public static CreateGameStudioCommand fromDto(CreateGameStudioDto studioDto, OwnerId ownerId) {
        return new CreateGameStudioCommand(
                GameStudioId.create(),
                ownerId,
                studioDto.name(),
                studioDto.description(),
                studioDto.IBAN(),
                studioDto.ownerFirstName(),
                studioDto.ownerLastName(),
                studioDto.ownerEmail()
        );
    }

    public static CreateGameStudioCommand fromDomain(GameStudio studio, OwnerId owner) {
        return new CreateGameStudioCommand(
                studio.getId(),
                owner,
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN(),
                "",
                "",
                ""
        );
    }
}

