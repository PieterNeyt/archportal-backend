package be.kdg.ip3.archportal.lobbies.infrastructure.lobby;

import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.GameId;
import be.kdg.ip3.archportal.lobbies.domain.lobby.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.session.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.infrastructure.lobby.jpa.JpaGameLobbyEntity;
import be.kdg.ip3.archportal.lobbies.infrastructure.lobby.jpa.JpaGameLobbyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DbLobbyRepository implements GameLobbyRepository {
    private final JpaGameLobbyRepository jpaLobbyRepository;

    public DbLobbyRepository(JpaGameLobbyRepository jpaLobbyRepository) {
        this.jpaLobbyRepository = jpaLobbyRepository;
    }

    @Override
    public void save(GameLobby lobby) {
        jpaLobbyRepository.save(JpaGameLobbyEntity.fromDomain(lobby));

    }

    @Override
    public Optional<GameLobby> findById(GameLobbyId id) {
        return jpaLobbyRepository.findById(id.id()).map(JpaGameLobbyEntity::toDomain);
    }
    @Override
    public Optional<GameLobby> findLobbyBySessionId(GameSessionId sessionId) {
        return jpaLobbyRepository.findLobbyBySessionId(sessionId.id())
                .map(JpaGameLobbyEntity::toDomain);
    }

    @Override
    public Optional<UUID> findPlayerBySessionId(GameSessionId sessionId) {
        return jpaLobbyRepository.findPlayerIdBySessionId(sessionId.id());
    }

    @Override
    public List<GameLobby> findAllLobbiesByGameId(GameId gameId) {
        return jpaLobbyRepository.findAllLobbiesByGameId(gameId.id()).stream()
                .map(JpaGameLobbyEntity::toDomain)
                .toList();
    }

    @Override
    public boolean isPlayerInLobby(PlayerId playerId) {
        return this.jpaLobbyRepository.existsByPlayersContains(playerId.id());
    }

    @Override
    public Optional<UUID> getLobbyIdFromPLayerID(PlayerId playerId) {
        return this.jpaLobbyRepository.findIdByPlayerId(playerId.id());
    }

    @Override
    public Optional<GameLobby> getLobbyFromPLayerID(PlayerId playerId) {
        return this.jpaLobbyRepository.findByPlayersContaining(playerId.id()).map(JpaGameLobbyEntity::toDomain);
    }

    @Override
    public void delete(GameLobby lobby) {
        this.jpaLobbyRepository.delete(JpaGameLobbyEntity.fromDomain(lobby));
    }
}
