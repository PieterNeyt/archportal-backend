package be.kdg.ip3.archportal.games.application.command;

import be.kdg.ip3.archportal.games.api.dto.GameStudioDto;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;

public record GameStudioCommand(
        GameStudioId id,
        OwnerId ownerId,
        String name,
        String description,
        String IBAN
) {
    public static GameStudioCommand fromDto(GameStudioDto studioDto, OwnerId ownerId) {
        return new GameStudioCommand(
                new GameStudioId(studioDto.id()),
                ownerId,
                studioDto.name(),
                studioDto.description(),
                studioDto.IBAN()
        );
    }

    public static GameStudioCommand fromDomain(GameStudio studio, OwnerId owner) {
        return new GameStudioCommand(
                studio.getId(),
                owner,
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN()
        );
    }

    public GameStudio toDomain() {
        return new GameStudio(id,ownerId,name,description,IBAN);
    }
}

