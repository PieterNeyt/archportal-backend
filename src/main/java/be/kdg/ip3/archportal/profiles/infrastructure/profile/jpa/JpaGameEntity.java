package be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa;

import be.kdg.ip3.archportal.profiles.domain.Library.Game;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class JpaGameEntity {
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private boolean favorite;


    public JpaGameEntity() {}

    public JpaGameEntity(UUID id, boolean favorite) {
        this.id = id;
        this.favorite = favorite;
    }

    public Game toDomain() {
        return new Game(id, favorite);
    }
}