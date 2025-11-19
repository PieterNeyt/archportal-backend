package be.kdg.ip3.archportal.gameService.api.dto;

import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;

import java.util.UUID;

public record GameStudioDto(
        UUID id,
        UUID ownerId,
        String name,
        String description,
        String IBAN
) {
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
