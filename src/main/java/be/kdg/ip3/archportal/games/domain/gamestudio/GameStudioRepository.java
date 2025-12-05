package be.kdg.ip3.archportal.games.domain.gamestudio;

import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface GameStudioRepository {
    void save(GameStudio studio);

    Optional<GameStudio> findById(GameStudioId id);

    Optional<GameStudio> findByOwnerId(OwnerId ownerId);
}
