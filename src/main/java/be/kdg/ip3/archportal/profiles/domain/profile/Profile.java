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
    private String originalIcon;
    private String gamerTag;
    private final List<Game> games;
    private int platformPoints;
    private final Set<UUID> platformBenefits;
    private final Set<FriendRequest> incomingFriendRequests;

    private UUID activeProfilePictureId;
    private UUID activeUsernameColorId;

    public Profile(ProfileId profileId, Set<UUID> platformBenefits, int platformPoints, String lastName, String email, String icon,
                   String gamerTag, String firstName, List<Game> games, Set<FriendRequest> incomingFriendRequests, String originalIcon, UUID activeProfilePictureId, UUID activeUsernameColorId) {
        this.id = profileId;
        setEmail(email);
        this.platformBenefits = platformBenefits;
        setPlatformPoints(platformPoints);
        setLastName(lastName);
        setIcon(icon);
        setGamerTag(gamerTag);
        setFirstName(firstName);
        this.games = games;
        this.incomingFriendRequests = incomingFriendRequests;
        this.activeProfilePictureId = activeProfilePictureId;
        this.activeUsernameColorId = activeUsernameColorId;
        this.originalIcon = originalIcon;
    }

    public static Profile createProfile(ProfileId profileId, String firstName, String lastName, String gamerTag, String email, String icon) {
        return new Profile(profileId, new HashSet<>(), 0, lastName, email, icon, gamerTag, firstName, new ArrayList<>(), new HashSet<>(),null,null,null);

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
    public void removePlatformBenefit(UUID benefitId) {
        if (!platformBenefits.remove(benefitId)) {
            throw new IllegalArgumentException("Profile does not own this benefit.");
        }
    }
    public void removeFriendRequest(FriendRequest friendRequest) {
        if (friendRequest == null)
            throw new IllegalArgumentException("The friend request provided is invalid.");
        if (!incomingFriendRequests.contains(friendRequest))
            throw new IllegalArgumentException("Friend request does not exists.");
        incomingFriendRequests.remove(friendRequest);
    }

    public void activateProfilePictureBenefit(UUID benefitId, String configuration) {
        if (this.activeProfilePictureId == null) {
            this.originalIcon = this.icon;
        }
        this.activeProfilePictureId = benefitId;
        this.icon = configuration;
    }
    public void deactivateProfilePictureBenefit() {
        this.activeProfilePictureId = null;
        this.icon = originalIcon;
    }
    public void activateNameColourBenefit(UUID benefitId) {
        this.activeUsernameColorId = benefitId;
    }
    public void deactivateNameColourBenefit() {

        this.activeUsernameColorId = null;
    }


    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be lower than 0");
        }
        this.platformPoints += points;
    }
    public void acquirePlatformBenefit(UUID platformBenefit, int cost) {
        if (platformBenefit == null) {
            throw new IllegalArgumentException("Platform benefit cannot be null.");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }
        if (platformPoints < cost) {
            throw new IllegalArgumentException("Not enough platform points.");
        }
        if (!platformBenefits.add(platformBenefit)) {
            throw new IllegalArgumentException(
                    "Profile %s already acquired platform benefit: %s"
                            .formatted(id, platformBenefit)
            );
        }

        this.platformPoints -= cost;
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
        if (gameId == null) return false;
        return this.games.stream()
                .anyMatch(g -> g.getGameId().equals(gameId));
    }

    public void hasGameCheck(UUID gameId) {
        if (hasGame(gameId)) {
            throw new IllegalArgumentException(
                    "Profile %s already owns the game: %s"
                            .formatted(id, gameId)
            );
        }
    }

    public void acquireGame(UUID gameId) {
        this.games.add(Game.create(gameId));
    }

    public void update(String firstName, String lastName, String gamerTag, String email, String currentIcon, String keycloakIcon) {
        setFirstName(firstName);
        setLastName(lastName);
        setGamerTag(gamerTag);
        setEmail(email);
        setIcon(currentIcon);
        this.originalIcon = keycloakIcon;
    }

    public Game findGameInlibrary(UUID gameId) {
        if (gameId == null)
            throw new IllegalArgumentException("GameId cannot be null.");

        return games.stream()
                .filter(g -> g.getGameId().equals(gameId))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Game %s not found in profile %s"
                                .formatted(gameId, id)));
    }

    public void favorite(UUID gameId) {
        var game = findGameInlibrary(gameId);

        if (game.isFavorite())
            throw new IllegalArgumentException("Game is already marked as favorite.");

        game.favorite();
    }

    public void unfavorite(UUID gameId) {
        var game = findGameInlibrary(gameId);

        if (!game.isFavorite())
            throw new IllegalArgumentException("Game is already not marked as favorite.");

        game.unfavorite();
    }
}
