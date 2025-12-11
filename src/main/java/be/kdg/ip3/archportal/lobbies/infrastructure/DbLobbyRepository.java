package be.kdg.ip3.archportal.lobbies.infrastructure;

import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import be.kdg.ip3.archportal.lobbies.infrastructure.jpa.JpaGameLobbyEntity;
import be.kdg.ip3.archportal.lobbies.infrastructure.jpa.JpaGameLobbyRepository;
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


}
