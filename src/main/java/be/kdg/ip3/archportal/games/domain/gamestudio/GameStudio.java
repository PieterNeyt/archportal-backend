package be.kdg.ip3.archportal.games.domain.gamestudio;

import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.security.access.AccessDeniedException;

@Getter
@AggregateRoot
public class GameStudio {
    private final GameStudioId id;
    private final OwnerId ownerId;
    private String name;
    private String description;
    private String IBAN;

    public GameStudio(OwnerId ownerId, String name, String description, String IBAN) {
        this(GameStudioId.create(), ownerId, name, description, IBAN);
    }

    public GameStudio(GameStudioId id, OwnerId ownerId, String name, String description, String IBAN) {
        this.id = id;
        this.ownerId = ownerId;
        setName(name);
        setDescription(description);
        setIBAN(IBAN);

    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.isEmpty())
            throw new IllegalArgumentException("The provided name is empty");
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty() || description.isEmpty())
            throw new IllegalArgumentException("The provided description is empty");

        this.description = description;
    }

    public void setIBAN(String IBAN) {
        if (IBAN == null || IBAN.trim().isEmpty() || IBAN.isEmpty())
            throw new IllegalArgumentException("The provided IBAN is empty");
        this.IBAN = IBAN;
    }
    
    public void checkOwner(OwnerId ownerId) {
        if (!this.ownerId.equals(ownerId))
            throw new AccessDeniedException("This is not your game studio");
    }

    public void update(GameStudio domain,OwnerId ownerId) {
        checkOwner(ownerId);
        setName(domain.getName());
        setDescription(domain.getDescription());
        setIBAN(domain.getIBAN());
    }
}
