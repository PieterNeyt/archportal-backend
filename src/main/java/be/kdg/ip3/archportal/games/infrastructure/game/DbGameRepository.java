package be.kdg.ip3.archportal.games.infrastructure.game;

import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.infrastructure.game.jpa.JpaGameEntity;
import be.kdg.ip3.archportal.games.infrastructure.game.jpa.JpaGameRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

    @Override
    public List<Game> findAll() {
        return jpaGameRepository.findAll()
                .stream()
                .map(JpaGameEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Game> findById(UUID gameId) {
        return jpaGameRepository.findById(gameId).map(JpaGameEntity::toDomain);
    }

    @Override
    public List<Game> findAllById(List<UUID> gameIds) {
        return jpaGameRepository.findAllById(gameIds)
                .stream()
                .map(JpaGameEntity::toDomain)
                .toList();
    }

    @Override
    public List<Game> findByStudioId(GameStudioId id) {
        return jpaGameRepository.findByStudioId(id.id()).stream()
                .map(JpaGameEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByGameUrl(String gameUrl) {
        return jpaGameRepository.existsByGameUrl(gameUrl);
    }
}
