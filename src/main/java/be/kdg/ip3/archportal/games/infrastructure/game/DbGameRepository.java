package be.kdg.ip3.archportal.games.infrastructure.game;

import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.infrastructure.game.jpa.JpaGameEntity;
import be.kdg.ip3.archportal.games.infrastructure.game.jpa.JpaGameRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DbGameRepository implements GameRepository {
    private final JpaGameRepository jpaGameRepository;

    public DbGameRepository(JpaGameRepository jpaGameRepository) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Override
    public void save(Game game) {
        jpaGameRepository.save(JpaGameEntity.fromDomain(game));
    }

    @Override
    public boolean existsById(UUID gameId) {
        return jpaGameRepository.existsById(gameId);
    }
}
