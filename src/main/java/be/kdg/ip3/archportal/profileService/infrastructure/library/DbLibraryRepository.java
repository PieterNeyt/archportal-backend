package be.kdg.ip3.archportal.profileService.infrastructure.library;

import be.kdg.ip3.archportal.gameService.domain.NotFoundException;
import be.kdg.ip3.archportal.profileService.domain.library.Library;
import be.kdg.ip3.archportal.profileService.domain.library.LibraryRepository;
import be.kdg.ip3.archportal.profileService.infrastructure.library.jpa.JpaLibraryEntity;
import be.kdg.ip3.archportal.profileService.infrastructure.library.jpa.JpaLibraryRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class DbLibraryRepository implements LibraryRepository {

    private final JpaLibraryRepository jpaLibraryRepository;

    public DbLibraryRepository( JpaLibraryRepository jpaLibraryRepository) {
        this.jpaLibraryRepository = jpaLibraryRepository;

    }

    @Override
    public void save(Library library) {
        this.jpaLibraryRepository.save(JpaLibraryEntity.fromDomain(library));
    }

    @Override
    public Library findById(UUID id) {
        return jpaLibraryRepository.findById(id)
                .map(JpaLibraryEntity::toDomain)
                .orElseThrow(() -> new NotFoundException("No library found with id: "+id));
    }
}
