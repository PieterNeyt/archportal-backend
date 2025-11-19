package be.kdg.ip3.archportal.gameService.infrastructure.gamestudio.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGameStudioRepository extends JpaRepository<JpaGameStudioEntity,UUID> {
}
