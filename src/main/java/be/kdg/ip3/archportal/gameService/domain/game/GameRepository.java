package be.kdg.ip3.archportal.gameService.domain.game;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;

@Repository
public interface GameRepository {
    void save(Game game);
    List<Game> findAll();
}
