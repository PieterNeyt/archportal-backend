package be.kdg.ip3.archportal.gameService.infrastructure;

import be.kdg.ip3.archportal.gameService.domain.NotFoundException;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioRepository;
import be.kdg.ip3.archportal.gameService.infrastructure.gamestudio.jpa.JpaGameStudioEntity;
import be.kdg.ip3.archportal.gameService.infrastructure.gamestudio.jpa.JpaGameStudioRepository;
import be.kdg.ip3.archportal.gameService.infrastructure.owner.jpa.JpaOwnerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbGameStudioRepository  implements GameStudioRepository {
    private final JpaGameStudioRepository jpaGameStudioRepository;
    private final JpaOwnerRepository jpaOwnerRepository;

    public DbGameStudioRepository(JpaGameStudioRepository jpaGameStudioRepository, JpaOwnerRepository jpaOwnerRepository) {
        this.jpaGameStudioRepository = jpaGameStudioRepository;
        this.jpaOwnerRepository = jpaOwnerRepository;
    }

    @Override
    public void save(GameStudio studio) {
        var ownerEntity = jpaOwnerRepository.findById(studio.getOwnerId().id())
                .orElseThrow(() -> new NotFoundException("Owner not found"));
        var jpaStudio = JpaGameStudioEntity.fromDomain(studio,ownerEntity);
         this.jpaGameStudioRepository.save(jpaStudio);
    }
}
