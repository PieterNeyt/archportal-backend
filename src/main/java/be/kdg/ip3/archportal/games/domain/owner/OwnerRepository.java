package be.kdg.ip3.archportal.games.domain.owner;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface OwnerRepository {
    void save(Owner owner);

    boolean existsById(OwnerId ownerId);
}
