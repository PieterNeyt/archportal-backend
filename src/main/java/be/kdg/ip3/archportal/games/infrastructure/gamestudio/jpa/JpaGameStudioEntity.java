package be.kdg.ip3.archportal.games.infrastructure.gamestudio.jpa;

import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "gamestudio", schema = "gameservice")
public class JpaGameStudioEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String IBAN;


    protected JpaGameStudioEntity() {
    }

    public JpaGameStudioEntity(UUID id, UUID ownerId, String name, String description, String IBAN) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.IBAN = IBAN;
    }

    public static JpaGameStudioEntity fromDomain(GameStudio studio) {
        return new JpaGameStudioEntity(
                studio.getId().id(),
                studio.getOwnerId().id(),
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN()
        );
    }

    public GameStudio toDomain() {
        return new GameStudio(new GameStudioId(id),
                new OwnerId(ownerId),
                name,
                description,
                IBAN);
    }

}

