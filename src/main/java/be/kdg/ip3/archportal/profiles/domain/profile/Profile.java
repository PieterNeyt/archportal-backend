package be.kdg.ip3.archportal.profiles.domain.profile;

import be.kdg.ip3.archportal.profiles.domain.Library.Game;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestAlreadyExistsException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.InvalidFriendRequestException;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.*;

@Getter
@AggregateRoot
public class Profile {
    private final ProfileId id;
    private String firstName;
    private String lastName;
    private String email;
    private String icon;
    private String gamerTag;
    private final List<Game> games;
    private int platformPoints;
    private final List<UUID> platformBenefits;
    private final Set<FriendRequest> incomingFriendRequests;

    public Profile(ProfileId profileId, List<UUID> platformBenefits, int platformPoints, String lastName, String email, String icon,
                   String gamerTag, String firstName, List<Game> games, Set<FriendRequest> incomingFriendRequests) {
        this.id = profileId;
        setEmail(email);
        this.platformBenefits = platformBenefits;
        setPlatformPoints(platformPoints);
        setLastName(lastName);
        this.icon = icon;
        setGamerTag(gamerTag);
        setFirstName(firstName);
        this.games = games;
        this.incomingFriendRequests = incomingFriendRequests;
    }

    public static Profile createProfile(ProfileId profileId, String firstName, String lastName, String gamerTag, String email,String icon) {
        return new Profile(profileId, new ArrayList<>(), 0, lastName, email, icon, gamerTag, firstName, new ArrayList<>(), new HashSet<>());

    }

    private void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || email.isEmpty() || email.length() > 255)
            throw new IllegalArgumentException("The email provided is invalid.");

        this.email = email;
    }

    private void setIcon(String icon) {
        if (icon != null && icon.length() > 255)
            throw new IllegalArgumentException("The icon provided is invalid.");

        this.icon = icon;
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

    public void addIncomingFriendRequest(FriendRequest friendRequest) {
        if (friendRequest == null)
            throw new IllegalArgumentException("The friend request provided is invalid.");
        if (incomingFriendRequests.contains(friendRequest))
            throw new FriendRequestAlreadyExistsException("Friend request already exists.");

        incomingFriendRequests.add(friendRequest);
    }

    public void removeFriendRequest(FriendRequest friendRequest) {
        if (friendRequest == null)
            throw new IllegalArgumentException("The friend request provided is invalid.");
        if (!incomingFriendRequests.contains(friendRequest))
            throw new IllegalArgumentException("Friend request does not exists.");
        incomingFriendRequests.remove(friendRequest);
    }

    public void AddPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be lower than 0");
        }
        this.platformPoints += points;
    }

    public void validateNotSameProfile(Profile receiver) {
        if (this.id.equals(receiver.getId()))
            throw new InvalidFriendRequestException("Sender and receiver profiles cannot be the same profile.");
    }

    public FriendRequest getIncomingFriendRequest(ProfileId senderId) {
        return incomingFriendRequests.stream().filter(r -> r.senderId().equals(senderId))
                .findFirst().orElseThrow(() -> new NotFoundException("Friend request not found."));
    }

    public void validateNoExistingRequestBetween(Profile receiver) {
        boolean exists = receiver.getIncomingFriendRequests().stream()
                .anyMatch(r -> r.senderId().equals(receiver.getId())) ||
                incomingFriendRequests.stream()
                        .anyMatch(r -> r.senderId().equals(receiver.getId()));
        if (exists)
            throw new FriendRequestAlreadyExistsException("A pending friend request already exists between these profiles.");
    }

    public boolean hasGame(UUID gameId) {
        return this.games.contains(gameId);
    }

    public void hasGameCheck(UUID gameId) {
        if (this.games.contains(gameId)) {
            throw new IllegalArgumentException(
                    "Profile %s already owns the games: %s"
                            .formatted(id, gameId)
            );
        }
    }

    public void acquireGame(UUID gameId) {
        this.games.add(Game.create(gameId));
    }

    public void update(String firstName, String lastName, String gamerTag, String email,String icon) {
        setFirstName(firstName);
        setLastName(lastName);
        setGamerTag(gamerTag);
        setEmail(email);
        setIcon(icon);
    }
}
