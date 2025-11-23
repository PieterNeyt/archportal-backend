package be.kdg.ip3.archportal.profiles.domain.profile;

import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.List;
import java.util.UUID;

@Getter
@AggregateRoot
public class Profile {
    private final ProfileId id;
    private String firstName;
    private String lastName;
    private String icon;
    private String gamerTag;
    private List<UUID> library;
    private int platformPoints;
    private List<ProfileId> friends;
    private List<UUID> platformBenefits;

    public Profile(UUID profileId, List<UUID> platformBenefits, int platformPoints, String lastName, String icon, String gamerTag, List<ProfileId> friends, String firstName, List<UUID> library) {
        this(ProfileId.create(profileId), platformBenefits, platformPoints, lastName, icon, gamerTag, friends, firstName, library);
    }

    public Profile(ProfileId profileId, List<UUID> platformBenefits, int platformPoints, String lastName, String icon,
                   String gamerTag, List<ProfileId> friends, String firstName, List<UUID> library) {
        this.id = profileId;
        this.platformBenefits = platformBenefits;
        setPlatformPoints(platformPoints);
        setLastName(lastName);
        this.icon = icon;
        setGamerTag(gamerTag);
        this.friends = friends;
        setFirstName(firstName);
        this.library = library;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty() || firstName.isEmpty() || firstName.length() > 255)
            throw new IllegalArgumentException("The firstName provided is invalid.");
        this.firstName = firstName;
    }

    public void setLastName(String lastname) {
        if (lastname == null || lastname.trim().isEmpty() || lastname.isEmpty() || lastname.length() > 255)
            throw new IllegalArgumentException("The lastname provided is invalid.");
        this.lastName = lastname;
    }

    public void setGamerTag(String gamerTag) {
        if (gamerTag == null || gamerTag.trim().isEmpty() || gamerTag.isEmpty() || gamerTag.length() > 255)
            throw new IllegalArgumentException("The gamerTag provided is invalid.");
        this.gamerTag = gamerTag;
    }

    public void setPlatformPoints(int platformPoints) {
        if (platformPoints < 0)
            throw new IllegalArgumentException("The platformPoints provided are invalid.");
        this.platformPoints = platformPoints;
    }

    public void AddPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be lower than 0");
        }
        this.platformPoints += points;
    }

    public boolean hasGame(UUID gameId) {
        return this.library.contains(gameId);
    }

    public void acquireGame(UUID gameId) {
        this.library.add(gameId);
    }
}
