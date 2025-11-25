package be.kdg.ip3.archportal.lobbies.infrastructure;

import be.kdg.ip3.archportal.lobbies.domain.GameLobby;
import be.kdg.ip3.archportal.lobbies.domain.GameLobbyRepository;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.infrastructure.jpa.JpaGameLobbyEntity;
import be.kdg.ip3.archportal.lobbies.infrastructure.jpa.JpaGameLobbyRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
        return jpaLobbyRepository.findAll().stream()
                .filter(entity -> entity.getSessions().stream()
                        .anyMatch(sessionEntity -> sessionEntity.getGameSessionId().equals(sessionId.id())))
                .findFirst()
                .map(JpaGameLobbyEntity::toDomain);

    }


}
