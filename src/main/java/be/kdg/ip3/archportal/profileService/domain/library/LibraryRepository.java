package be.kdg.ip3.archportal.profileService.domain.library;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface LibraryRepository {
    void save(Library library);
}
