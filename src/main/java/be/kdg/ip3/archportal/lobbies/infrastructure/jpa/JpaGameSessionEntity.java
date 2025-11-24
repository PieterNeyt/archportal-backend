package be.kdg.ip3.archportal.lobbies.infrastructure.jpa;

import be.kdg.ip3.archportal.lobbies.domain.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_sessions")
public class JpaGameSessionEntity {
    @Id
    private UUID gameSessionId;

    @Column(nullable = false)
    private UUID gameLobbyId;

    @Column( nullable = false)
    private UUID playerId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;


    protected JpaGameSessionEntity() {}

    public static JpaGameSessionEntity fromDomain(GameSession session) {
        JpaGameSessionEntity e = new JpaGameSessionEntity();
        e.gameSessionId = session.getGameSessionId().id();
        e.gameLobbyId = session.getGameLobbyId().id();
        e.playerId = session.getPlayerId().id();
        e.startTime = session.getStartTime();
        e.endTime = session.getEndTime();
        return e;
    }

    public GameSession toDomain() {
        return new GameSession(
                new GameSessionId(gameSessionId),
                new GameLobbyId(gameLobbyId),
                new PlayerId(playerId),
                startTime,
                endTime
        );
    }
}
