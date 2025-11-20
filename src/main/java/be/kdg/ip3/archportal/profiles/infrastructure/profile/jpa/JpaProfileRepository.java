package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProfileRepository  extends JpaRepository<JpaProfileEntity, UUID> {
}
