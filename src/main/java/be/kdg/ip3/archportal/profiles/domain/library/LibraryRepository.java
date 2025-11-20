package be.kdg.ip3.archportal.profiles.domain.library;

import org.jmolecules.ddd.annotation.Repository;

import java.util.UUID;

@Repository
public interface LibraryRepository {
    void save(Library library);
    Library findById(UUID id);
}
