package be.kdg.ip3.archportal.games.domain.owner;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

@Getter
@Entity
public class Owner {
    private final OwnerId id;
    private String firstName;
    private String lastName;
    private String email;

    public Owner(OwnerId id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Owner(String firstName, String lastName, String email) {
        this.id = OwnerId.create();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
