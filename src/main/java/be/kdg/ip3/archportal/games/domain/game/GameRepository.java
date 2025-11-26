package be.kdg.ip3.archportal.games.domain.game;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;
import java.util.UUID;

import java.util.List;

@Repository
public interface GameRepository {
    void save(Game game);
    boolean existsById(UUID gameId);
    List<Game> findAll();
    Optional<Game> findById(UUID gameId);
    List<Game> findAllById(List<UUID> gameIds);
}
