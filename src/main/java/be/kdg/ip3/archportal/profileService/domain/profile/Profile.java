package be.kdg.ip3.archportal.profileService.domain.profile;

import be.kdg.ip3.archportal.gameService.domain.Money;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.math.BigDecimal;
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
    //:TODO --> de library moet worden aangemaakt en mee gekoppeld worden bij aanmaken account
    private UUID libraryId;
    private int platformPoints;
    private List<ProfileId> friends;
    private List<UUID> platformBenefits;

    public Profile(List<UUID> platformBenefits, int platformPoints, UUID libraryId, String lastName, String icon, String gamerTag, List<ProfileId> friends, String firstName) {
        this(ProfileId.create(), platformBenefits, platformPoints, libraryId, lastName, icon, gamerTag, friends, firstName);
    }

    public Profile(ProfileId profileId,List<UUID> platformBenefits, int platformPoints, UUID libraryId, String lastName, String icon, String gamerTag, List<ProfileId> friends, String firstName) {

        setPlatformPoints(platformPoints);
        setLastName(lastName);
        setGamerTag(gamerTag);
        setFirstName(firstName);
        this.platformBenefits = platformBenefits;
        this.icon = icon;
        this.friends = friends;
        this.id = profileId;
        this.libraryId = libraryId;
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
        if(points<0){
            throw new IllegalArgumentException("Points cannot be lower than 0");
        }
        this.platformPoints += points;
    }
}
