package be.kdg.ip3.archportal.gameService.domain.owner;

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
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public Owner(String firstName, String lastName, String email) {
        this(OwnerId.create(), firstName, lastName, email);
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty() || firstName.isEmpty())
            throw new IllegalArgumentException("The provided firstName is empty");

        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty() || lastName.isEmpty())
            throw new IllegalArgumentException("The provided lastName is empty");

        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || email.isEmpty())
            throw new IllegalArgumentException("The provided setEmail is empty");

        this.email = email;
    }
}
