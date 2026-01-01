package be.kdg.ip3.archportal.lobbies.infrastructure.lobby.jpa;

import be.kdg.ip3.archportal.lobbies.domain.ChatRoomId;
import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobbyStatus;
import be.kdg.ip3.archportal.lobbies.domain.GameId;
import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_lobbies", schema = "lobbyservice")
@Getter
public class JpaGameLobbyEntity {
    @Id
    private UUID gameLobbyId;

    @Column(nullable = false)
    private UUID gameId;

    @Column(nullable = false)
    private int maxPlayers;

    @Enumerated(EnumType.STRING)
    private GameLobbyStatus gameLobbyStatus;

    @ElementCollection
    @CollectionTable(
            name = "game_lobby_players",
            schema = "lobbyservice",
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
    @Column
    private UUID chatRoomId;

    protected JpaGameLobbyEntity() {
    }

    public JpaGameLobbyEntity(UUID gameLobbyId, UUID gameId, int maxPlayers, GameLobbyStatus gameLobbyStatus, List<UUID> players, List<JpaGameSessionEntity> sessions, UUID chatRoomId) {
        this.gameLobbyId = gameLobbyId;
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.gameLobbyStatus = gameLobbyStatus;
        this.players = players;
        this.sessions = sessions;
        this.chatRoomId = chatRoomId;
    }

    public static JpaGameLobbyEntity fromDomain(GameLobby gameLobby) {
//        JpaGameLobbyEntity entity = new JpaGameLobbyEntity();
//        entity.gameLobbyId = gameLobby.getGameLobbyId().id();
//        entity.gameId = gameLobby.getGameId().id();
//        entity.maxPlayers = gameLobby.getMaxPlayers();
//        entity.gameLobbyStatus = gameLobby.getGameLobbyStatus();
//        entity.players = gameLobby.getPlayers().stream().map(PlayerId::id).toList();
//        entity.sessions = gameLobby.getSessions().stream()
//                .map(JpaGameSessionEntity::fromDomain)
//                .toList();
//        return entity;
        return new JpaGameLobbyEntity(
                gameLobby.getGameLobbyId().id(),
                gameLobby.getGameId().id(),
                gameLobby.getMaxPlayers(),
                gameLobby.getGameLobbyStatus(),
                gameLobby.getPlayers().stream().map(PlayerId::id).toList(),
                gameLobby.getSessions().stream().map(JpaGameSessionEntity::fromDomain).toList(),
                gameLobby.getChatRoomId().id()
        );
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
                        .toList(),
                new ChatRoomId(chatRoomId)
        );
    }


}
