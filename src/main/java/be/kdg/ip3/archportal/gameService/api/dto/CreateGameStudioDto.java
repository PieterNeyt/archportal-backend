package be.kdg.ip3.archportal.gameService.api.dto;

import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;

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
    public static CreateGameStudioDto fromDomain(GameStudio studio) {
        return new CreateGameStudioDto(
                studio.getId().id(),
                studio.getOwnerId().id(),
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN(),
                "",
                "",
                ""

        );
    }
}

