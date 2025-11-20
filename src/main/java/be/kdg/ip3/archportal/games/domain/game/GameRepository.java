package be.kdg.ip3.archportal.games.domain.game;

import org.jmolecules.ddd.annotation.Repository;

import java.util.UUID;

@Repository
public interface GameRepository {
    void save(Game game);
    boolean existsById(UUID gameId);
}
