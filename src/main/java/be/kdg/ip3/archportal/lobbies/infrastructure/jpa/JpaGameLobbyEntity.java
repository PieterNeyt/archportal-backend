package be.kdg.ip3.archportal.lobbies.infrastructure.jpa;

import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyStatus;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_lobbies")
@Getter
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

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "game_lobby_id")
    private List<JpaGameSessionEntity> sessions;

    protected  JpaGameLobbyEntity() {}

    public static JpaGameLobbyEntity fromDomain(GameLobby gameLobby) {
        JpaGameLobbyEntity entity = new JpaGameLobbyEntity();
        entity.gameLobbyId = gameLobby.getGameLobbyId().id();
        entity.gameId = gameLobby.getGameId().id();
        entity.maxPlayers = gameLobby.getMaxPlayers();
        entity.gameLobbyStatus = gameLobby.getGameLobbyStatus();
        entity.players = gameLobby.getPlayers().stream().map(PlayerId::id).toList();
        entity.sessions = gameLobby.getSessions().stream()
                .map(JpaGameSessionEntity::fromDomain)
                .toList();        return entity;
    }
    public GameLobby toDomain() {
        var lobbyId = new GameLobbyId(gameLobbyId);

        return new GameLobby(
                lobbyId,
                new GameId(gameId),
                maxPlayers,
                gameLobbyStatus,
                players.stream().map(PlayerId::new).toList(),
                sessions.stream()
                        .map(s -> s.toDomain(lobbyId))
                        .toList()
        );
    }



}
