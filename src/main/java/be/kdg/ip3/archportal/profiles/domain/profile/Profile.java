package be.kdg.ip3.archportal.profiles.domain.profile;

import be.kdg.ip3.archportal.profiles.domain.friendRequest.AlreadyFriendException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
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
    private List<UUID> library;
    private int platformPoints;
    private Set<ProfileId> friends;
    private List<UUID> platformBenefits;
    private final Set<FriendRequest> incomingFriendRequests;

    public Profile(ProfileId profileId, List<UUID> platformBenefits, int platformPoints, String lastName, String email, String icon,
                   String gamerTag, Set<ProfileId> friends, String firstName, List<UUID> library, Set<FriendRequest> incomingFriendRequests) {
        this.id = profileId;
        setEmail(email);
        this.platformBenefits = platformBenefits;
        setPlatformPoints(platformPoints);
        setLastName(lastName);
        this.icon = icon;
        setGamerTag(gamerTag);
        this.friends = friends;
        setFirstName(firstName);
        this.library = library;
        this.incomingFriendRequests = incomingFriendRequests;
    }

    public static Profile createProfile(ProfileId profileId, String firstName, String lastName, String gamerTag, String email) {
        return new Profile(profileId, new ArrayList<>(), 0, lastName, email, "", gamerTag, new HashSet<>(), firstName, new ArrayList<>(), new HashSet<>());

    }

    private void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || email.isEmpty() || email.length() > 255)
            throw new IllegalArgumentException("The email provided is invalid.");

        this.email = email;
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

    private void addFriend(ProfileId friendId) {
        if (friendId == null)
            throw new IllegalArgumentException("The friend id provided is invalid.");

        if (friends.contains(friendId))
            throw new IllegalArgumentException("Friend already exists.");

        friends.add(friendId);
    }

    public void addIncomingFriendRequest(FriendRequest friendRequest) {
        if (friendRequest == null)
            throw new IllegalArgumentException("The friend request provided is invalid.");
        if (incomingFriendRequests.contains(friendRequest))
            throw new IllegalArgumentException("Friend request already exists.");

        incomingFriendRequests.add(friendRequest);
    }

    public void acceptFriendRequest(FriendRequest friendRequest) {
        if (friendRequest == null)
            throw new IllegalArgumentException("The friend request provided is invalid.");
        if (!incomingFriendRequests.contains(friendRequest))
            throw new IllegalArgumentException("Friend request does not exists.");

        addFriend(friendRequest.senderId());

        incomingFriendRequests.remove(friendRequest);
    }
    
    public void declineFriendRequest(FriendRequest friendRequest) {
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
    
    public void validateNotAlreadyFriends(Profile receiver) {
        if (this.friends.contains(receiver.getId()))
            throw new AlreadyFriendException(receiver.getGamerTag());
    }

    public boolean hasGame(UUID gameId) {
        return this.library.contains(gameId);
    }

    public void hasGameCheck(UUID gameId) {
        if (this.library.contains(gameId)) {
            throw new IllegalArgumentException(
                    "Profile %s already owns the games: %s"
                            .formatted(id, gameId)
            );
        }
    }

    public void acquireGame(UUID gameId) {
        this.library.add(gameId);
    }
}
