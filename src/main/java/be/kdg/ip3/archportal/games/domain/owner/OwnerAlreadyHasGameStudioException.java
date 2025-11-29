package be.kdg.ip3.archportal.games.domain.owner;

public class OwnerAlreadyHasGameStudioException extends RuntimeException {
    public OwnerAlreadyHasGameStudioException(OwnerId ownerId) {
        super("Owner [" + ownerId.id() + "] already has a game studio");
    }
}
