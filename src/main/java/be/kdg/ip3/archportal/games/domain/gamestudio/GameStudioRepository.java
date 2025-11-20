package be.kdg.ip3.archportal.games.domain.gamestudio;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface GameStudioRepository {
    void save(GameStudio studio);

    Optional<GameStudio> findById(GameStudioId id);
}
