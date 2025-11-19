package be.kdg.ip3.archportal.gameService.infrastructure.owner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOwnerRepository extends JpaRepository<JpaOwnerEntity, UUID> {
}
