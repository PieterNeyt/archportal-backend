package be.kdg.ip3.archportal.games.infrastructure.gamestudio;

import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import be.kdg.ip3.archportal.games.infrastructure.gamestudio.jpa.JpaGameStudioEntity;
import be.kdg.ip3.archportal.games.infrastructure.gamestudio.jpa.JpaGameStudioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DbGameStudioRepository implements GameStudioRepository {
    private final JpaGameStudioRepository jpaGameStudioRepository;

    public DbGameStudioRepository(JpaGameStudioRepository jpaGameStudioRepository) {
        this.jpaGameStudioRepository = jpaGameStudioRepository;
    }

    @Override
    public Optional<GameStudio> findById(GameStudioId id) {
        return this.jpaGameStudioRepository.findById(id.id()).map(JpaGameStudioEntity::toDomain);
    }

    @Override
    public Optional<GameStudio> findByOwnerId(OwnerId ownerId) {
        return this.jpaGameStudioRepository.findByOwnerId(ownerId.id()).map(JpaGameStudioEntity::toDomain);
    }

    @Override
    public void save(GameStudio studio) {
        var jpaStudio = JpaGameStudioEntity.fromDomain(studio);
        this.jpaGameStudioRepository.save(jpaStudio);
    }
}
