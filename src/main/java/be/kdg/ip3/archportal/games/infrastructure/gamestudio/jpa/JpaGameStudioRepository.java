package be.kdg.ip3.archportal.games.infrastructure.gamestudio.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaGameStudioRepository extends JpaRepository<JpaGameStudioEntity,UUID> {
    Optional<JpaGameStudioEntity> findByOwnerId(UUID ownerId);
}
