package be.kdg.ip3.archportal.gameService.infrastructure;

import be.kdg.ip3.archportal.gameService.domain.owner.Owner;
import be.kdg.ip3.archportal.gameService.domain.owner.OwnerRepository;
import be.kdg.ip3.archportal.gameService.infrastructure.owner.jpa.JpaOwnerEntity;
import be.kdg.ip3.archportal.gameService.infrastructure.owner.jpa.JpaOwnerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbOwnerRepository implements OwnerRepository {
    private final JpaOwnerRepository jpaOwnerRepository;
    public DbOwnerRepository(JpaOwnerRepository jpaOwnerRepository) {
        this.jpaOwnerRepository = jpaOwnerRepository;
    }

    @Override
    public void save(Owner owner) {
        this.jpaOwnerRepository.save(JpaOwnerEntity.fromDomain(owner));
    }
}
