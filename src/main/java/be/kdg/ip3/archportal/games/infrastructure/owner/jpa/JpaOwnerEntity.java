package be.kdg.ip3.archportal.games.infrastructure.owner.jpa;

import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.Owner;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "owner", schema = "gameservice")
public class JpaOwnerEntity {
    @Id()
    private UUID id;

    @Column(nullable = false)
    private UUID gameStudioId;

    public JpaOwnerEntity() {
    }

    public JpaOwnerEntity(UUID ownerId, UUID gameStudioId) {
        this.id = ownerId;
        this.gameStudioId = gameStudioId;
    }

    public static JpaOwnerEntity fromDomain(Owner owner) {
        return new JpaOwnerEntity(
                owner.getId().id(),
                owner.getGameStudioId().id()
        );
    }

    public Owner toDomain() {
        return new Owner(
                new OwnerId(id),
                new GameStudioId(gameStudioId));
    }
}
