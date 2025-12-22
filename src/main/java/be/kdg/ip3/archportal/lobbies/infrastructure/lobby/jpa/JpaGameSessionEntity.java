package be.kdg.ip3.archportal.lobbies.infrastructure.lobby.jpa;

import be.kdg.ip3.archportal.lobbies.domain.session.GameSession;
import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.session.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_sessions", schema = "lobbyservice")
@Getter
public class JpaGameSessionEntity {

    @Id
    private UUID gameSessionId;

    @Column(nullable = false)
    private UUID playerId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String launchUrl;

    protected JpaGameSessionEntity() {}

    public static JpaGameSessionEntity fromDomain(GameSession s) {
        JpaGameSessionEntity e = new JpaGameSessionEntity();
        e.gameSessionId = s.getGameSessionId().id();
        e.playerId = s.getPlayerId().id();
        e.startTime = s.getStartTime();
        e.endTime = s.getEndTime();
        e.launchUrl = s.getLaunchUrl();
        return e;
    }

    public GameSession toDomain(GameLobbyId lobbyId) {
        return new GameSession(
                new GameSessionId(gameSessionId),
                lobbyId,
                new PlayerId(playerId),
                startTime,
                endTime,
                launchUrl
        );
    }
}

