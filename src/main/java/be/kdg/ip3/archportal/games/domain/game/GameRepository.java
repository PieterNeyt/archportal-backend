package be.kdg.ip3.archportal.games.domain.game;

import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository {
    void save(Game game);
    boolean existsById(UUID gameId);
    List<Game> findAll();
    Optional<Game> findById(UUID gameId);
    List<Game> findAllById(List<UUID> gameIds);

    List<Game> findByStudioId(GameStudioId id);

}
