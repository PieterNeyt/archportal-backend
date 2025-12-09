package be.kdg.ip3.archportal.games.application.command;

import be.kdg.ip3.archportal.games.api.dto.GameStudioDto;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;

import java.util.UUID;

public record GameStudioCommand(
        GameStudioId id,
        OwnerId ownerId,
        String name,
        String description,
        String IBAN
) {
    public static GameStudioCommand fromDto(GameStudioDto studioDto, OwnerId ownerId) {
        GameStudioId id;
        if (studioDto.id() == null) {
            id = GameStudioId.create();
        } else {
            id = new GameStudioId(studioDto.id());
        }

        return new GameStudioCommand(
                id,
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

