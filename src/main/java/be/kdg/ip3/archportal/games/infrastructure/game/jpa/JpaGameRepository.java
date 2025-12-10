package be.kdg.ip3.archportal.games.infrastructure.game.jpa;

import be.kdg.ip3.archportal.games.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaGameRepository extends JpaRepository<JpaGameEntity, UUID> {
    List<JpaGameEntity> findByStudioId(UUID studioId);
}
