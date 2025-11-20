package be.kdg.ip3.archportal.games.domain.game;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface GameRepository {
    void save(Game game);
}
