package be.kdg.ip3.archportal.games.infrastructure.game.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaGameRepository extends JpaRepository<JpaGameEntity, UUID> {
    List<JpaGameEntity> findByStudioId(UUID studioId);

    boolean existsByGameUrl(String gameUrl);

    @Query("select g.id from JpaGameEntity g where g.title = :name")
    Optional<UUID> findGameIdByName(@Param("name") String name);
}
