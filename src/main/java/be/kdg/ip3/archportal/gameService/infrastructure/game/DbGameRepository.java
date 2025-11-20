package be.kdg.ip3.archportal.gameService.infrastructure.game;

import be.kdg.ip3.archportal.gameService.domain.game.Game;
import be.kdg.ip3.archportal.gameService.domain.game.GameRepository;
import be.kdg.ip3.archportal.gameService.infrastructure.game.jpa.JpaGameEntity;
import be.kdg.ip3.archportal.gameService.infrastructure.game.jpa.JpaGameRepository;
import org.springframework.stereotype.Repository;

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
}
