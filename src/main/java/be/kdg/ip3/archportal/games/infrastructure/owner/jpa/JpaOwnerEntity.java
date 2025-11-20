package be.kdg.ip3.archportal.games.infrastructure.owner.jpa;

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
@Table(name = "owner",schema = "gameservice")
public class JpaOwnerEntity {
    @Id()
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    public JpaOwnerEntity() {}

    public JpaOwnerEntity(UUID ownerId, String firstName, String lastName, String email) {
        this.id = ownerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static JpaOwnerEntity fromDomain(Owner owner) {
        return new JpaOwnerEntity(
                owner.getId().id(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getEmail()
        );
    }

    public Owner toDomain() {
        return new Owner(
                new OwnerId(id),
                firstName,
                lastName,
                email);
    }
}
