package be.kdg.ip3.archportal.lobbies.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGameLobbyRepository extends JpaRepository<JpaGameLobbyEntity, UUID> {
}
