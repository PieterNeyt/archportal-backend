package be.kdg.ip3.archportal.profiles.domain.Library;

import lombok.Getter;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@Getter
@ValueObject
public class Game {
    private final UUID gameId;
    private boolean favorite;

    public Game(UUID gameId, boolean favorite) {
        if (gameId == null)
            throw new IllegalArgumentException("gameId cannot be null");

        this.gameId = gameId;
        this.favorite = favorite;
    }

    public static Game create(UUID gameId) {
        return new Game(gameId, false);
    }

    public void favorite() {
        if (this.favorite)
            throw new IllegalStateException("Game is already marked as favorite.");

        this.favorite = true;
    }

    public void unfavorite() {
        if (!this.favorite)
            throw new IllegalStateException("Game is not marked as favorite.");

        this.favorite = false;
    }
}
