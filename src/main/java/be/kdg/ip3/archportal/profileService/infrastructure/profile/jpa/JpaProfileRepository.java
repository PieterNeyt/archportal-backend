package be.kdg.ip3.archportal.profileService.infrastructure.profile.jpa;

import be.kdg.ip3.archportal.profileService.infrastructure.library.jpa.JpaLibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProfileRepository  extends JpaRepository<JpaProfileEntity, UUID> {
}
