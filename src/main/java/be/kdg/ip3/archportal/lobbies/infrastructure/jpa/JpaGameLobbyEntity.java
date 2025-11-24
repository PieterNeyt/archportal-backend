package be.kdg.ip3.archportal.lobbies.infrastructure.jpa;

import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyStatus;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_lobbies")
public class JpaGameLobbyEntity {
    @Id
    private UUID gameLobbyId;

    @Column(length = 255, nullable = false)
    private UUID gameId;

    @Column(nullable = false)
    private int maxPlayers;

    @Enumerated(EnumType.STRING)
    private GameLobbyStatus gameLobbyStatus;

    @ElementCollection
    @CollectionTable(
            name = "game_lobby_players",
            joinColumns = @JoinColumn(name = "game_lobby_id")
    )
    @Column(name = "player_id", nullable = false)
    private List<UUID> players;

    @ElementCollection
    @CollectionTable(
            name = "game_lobby_sessions",
            joinColumns = @JoinColumn(name = "game_lobby_id")
    )
    @Column(name = "session_id", nullable = false)
    private List<UUID> sessions;

    protected  JpaGameLobbyEntity() {}

    public static JpaGameLobbyEntity fromDomain(GameLobby gameLobby) {
        JpaGameLobbyEntity entity = new JpaGameLobbyEntity();
        entity.gameLobbyId = gameLobby.getGameLobbyId().id();
        entity.gameId = gameLobby.getGameId().id();
        entity.maxPlayers = gameLobby.getMaxPlayers();
        entity.gameLobbyStatus = gameLobby.getGameLobbyStatus();
        entity.players = gameLobby.getPlayers().stream().map(PlayerId::id).toList();
        entity.sessions = gameLobby.getSessions().stream().map(GameSessionId::id).toList();
        return entity;
    }

    public GameLobby toDomain() {
        GameLobby lobby = new GameLobby(
                new GameLobbyId(gameLobbyId),
                new GameId(gameId),
                maxPlayers,
                gameLobbyStatus,
                players.stream().map(PlayerId::new).toList(),
                sessions.stream().map(GameSessionId::new).toList()
        );
        return lobby;
    }

}
