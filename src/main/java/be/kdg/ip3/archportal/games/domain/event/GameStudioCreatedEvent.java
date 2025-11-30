package be.kdg.ip3.archportal.games.domain.event;

import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;

public record GameStudioCreatedEvent(OwnerId ownerId, GameStudioId gameStudioId) {
}
