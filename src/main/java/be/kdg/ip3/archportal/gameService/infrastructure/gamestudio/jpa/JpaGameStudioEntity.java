package be.kdg.ip3.archportal.gameService.infrastructure.gamestudio.jpa;

import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudio;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.gameService.domain.owner.OwnerId;
import be.kdg.ip3.archportal.gameService.infrastructure.owner.jpa.JpaOwnerEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "gamestudio", schema = "gameservice")
public class JpaGameStudioEntity {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private JpaOwnerEntity owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String IBAN;


    protected JpaGameStudioEntity() {
    }

    public JpaGameStudioEntity(UUID id, JpaOwnerEntity owner, String name, String description, String IBAN) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.IBAN = IBAN;
    }

    public static JpaGameStudioEntity fromDomain(GameStudio studio, JpaOwnerEntity owner) {
        return new JpaGameStudioEntity(
                studio.getId().id(),
                owner,
                studio.getName(),
                studio.getDescription(),
                studio.getIBAN()
        );
    }

    public GameStudio toDomain() {
        return new GameStudio(new GameStudioId(id),
                new OwnerId(owner.getId()),
                name,
                description,
                IBAN);
    }

}

