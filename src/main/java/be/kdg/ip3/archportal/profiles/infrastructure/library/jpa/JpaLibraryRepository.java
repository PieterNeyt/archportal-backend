package be.kdg.ip3.archportal.profiles.infrastructure.library.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLibraryRepository  extends JpaRepository<JpaLibraryEntity, UUID> {
}
