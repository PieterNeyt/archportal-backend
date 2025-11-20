package be.kdg.ip3.archportal.profileService.infrastructure.library.jpa;

import be.kdg.ip3.archportal.gameService.infrastructure.owner.jpa.JpaOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLibraryRepository  extends JpaRepository<JpaLibraryEntity, UUID> {
}
