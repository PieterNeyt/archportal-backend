package be.kdg.ip3.archportal.lobbies.infrastructure.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "game_lobbies")
public class JpaGameLobbyEntity {
    @Id
    private UUID gameLobbyId;

}
