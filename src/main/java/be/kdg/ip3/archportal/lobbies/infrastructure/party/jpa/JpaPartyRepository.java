package be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPartyRepository extends JpaRepository<JpaPartyEntity, UUID> {
}
