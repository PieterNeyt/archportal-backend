package be.kdg.ip3.archportal.profiles.domain.Library;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record Game(UUID gameId, boolean favorite) {
    public static Game create(UUID gameId){
        if(gameId == null)
            throw new IllegalArgumentException("gameId cannot be null");

        return new Game(gameId,false);
    }
}
