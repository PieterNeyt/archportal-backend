package be.kdg.ip3.archportal.gameService.infrastructure;

import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.gameService.infrastructure.gamestudio.jpa.JpaGameStudioEntity;
import be.kdg.ip3.archportal.gameService.infrastructure.gamestudio.jpa.JpaGameStudioRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DbGameStudioRepository  implements GameStudioRepository {
    private final JpaGameStudioRepository jpaGameStudioRepository;
    public DbGameStudioRepository(JpaGameStudioRepository jpaGameStudioRepository) {
        this.jpaGameStudioRepository = jpaGameStudioRepository;
    }

    @Override
    public Optional<GameStudio> findById(GameStudioId id) {
        return this.jpaGameStudioRepository.findById(id.id()).map(JpaGameStudioEntity::toDomain);
    }

    @Override
    public void save(GameStudio studio) {
        JpaGameStudioEntity jpaStudio = JpaGameStudioEntity.fromDomain(studio);
         this.jpaGameStudioRepository.save(jpaStudio);
    }
}
