package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.CreateNotificationSettingsEvent;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.shared.FriendShipCreatedEvent;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.AlreadyFriendException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestAction;
import be.kdg.ip3.archportal.profiles.domain.friendship.Friendship;
import be.kdg.ip3.archportal.profiles.domain.friendship.FriendshipRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final GamesApi gamesApi;
    private final ApplicationEventPublisher eventPublisher;
    private final FriendshipRepository friendshipRepository;

    public ProfileService(ProfileRepository profileRepository, GamesApi gamesApi, ApplicationEventPublisher eventPublisher, FriendshipRepository friendshipRepository) {
        this.profileRepository = profileRepository;
        this.gamesApi = gamesApi;
        this.eventPublisher = eventPublisher;
        this.friendshipRepository = friendshipRepository;
    }

    public Profile syncUser(Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        String firstName = token.getClaim("given_name");
        String lastName = token.getClaim("family_name");
        String gamerTag = token.getClaim("preferred_username");
        String icon = token.getClaim("icon");
        String email = token.getClaim("email");

        var profile = profileRepository.findById(profileId).orElseGet(() -> {
            Profile newProfile = Profile.createProfile(profileId, firstName, lastName, gamerTag, email,icon);
            eventPublisher.publishEvent(new CreateNotificationSettingsEvent(profileId.id()));
            return newProfile;
        });


        profile.update(firstName,lastName,gamerTag,email,icon);

        profileRepository.save(profile);
        return profile;
    }

    public List<GlobalGameDto> getLibrary(ProfileId profileId) {
        var profile = profileRepository.findById(profileId).orElseThrow(profileId::notFound);
        var gameIds = profile.getLibrary();

        return gamesApi.getGamesByIds(gameIds);
    }

    public List<Profile> getAllFriends(ProfileId profileId) {
        return profileRepository.findAllFriends(profileId);
    }

    public FriendRequest createFriendRequest(ProfileId senderId, String gamerTag) {
        var receiver = profileRepository.findByGamerTag(gamerTag).orElseThrow(() -> new NotFoundException("Profile with gamertag " + gamerTag + " is not found."));
        var sender = profileRepository.findById(senderId).orElseThrow(() -> new NotFoundException("Sender with id " + senderId + " is not found."));

        sender.validateNotSameProfile(receiver);
        sender.validateNoExistingRequestBetween(receiver);
        if (friendshipRepository.existsBetween(receiver.getId(), sender.getId()))
            throw new AlreadyFriendException(receiver.getGamerTag());

        var friendRequest = new FriendRequest(senderId);
        receiver.addIncomingFriendRequest(friendRequest);

        profileRepository.save(receiver);

        eventPublisher.publishEvent(new AddNotificationEvent(receiver.getId().id(),
                String.format("Friend request sent to %s", receiver.getGamerTag()),
                """
                        Your invite is on its way!
                        
                        You’ve successfully sent a friend request. Once it’s accepted, you’ll be able to start chatting, playing together, and sharing new experiences.
                        
                        Until then, feel free to keep exploring and connecting with other players across the platform.
                        
                        Kind regards,
                        The Arch Portal Team""", NotificationType.FRIEND_REQUEST));
        return friendRequest;
    }

    public List<Profile> findAllProfilesIncomingRequests(ProfileId receiverId) {
        var receiver = profileRepository.findById(receiverId).orElseThrow(receiverId::notFound);
        var senderIds = receiver.getIncomingFriendRequests().stream().map(FriendRequest::senderId).toList();
        return profileRepository.findFromIds(senderIds);
    }

    public List<Profile> findAllProfilesOutgoingRequests(ProfileId senderId) {
        return profileRepository.findByIncomingRequestHasId(senderId);
    }

    public void acceptFriendRequest(ProfileId receiverId, String gamerTag) {
        handleFriendRequest(receiverId, gamerTag, FriendRequestAction.ACCEPT);
    }

    public void declineFriendRequest(ProfileId receiverId, String gamerTag) {
        handleFriendRequest(receiverId, gamerTag, FriendRequestAction.DECLINE);
    }

    private void handleFriendRequest(ProfileId receiverId, String gamerTag, FriendRequestAction action) {
        var sender = profileRepository.findByGamerTag(gamerTag).orElseThrow(() -> new NotFoundException("Profile with gamertag " + gamerTag + " is not found."));
        var receiver = profileRepository.findById(receiverId).orElseThrow(receiverId::notFound);

        var request = receiver.getIncomingFriendRequests().stream().filter(r -> r.senderId().equals(sender.getId()))
                .findFirst().orElseThrow(() -> new NotFoundException("Friend request not found."));

        if (friendshipRepository.existsBetween(receiver.getId(), sender.getId())) {
            receiver.removeFriendRequest(request);
            profileRepository.save(receiver);
            throw new AlreadyFriendException(receiver.getGamerTag());
        }

        String title = "";
        String body = "";
        switch (action) {
            case ACCEPT -> {
                receiver.removeFriendRequest(request);
                var friendship = new Friendship(sender.getId(), receiver.getId());
                friendshipRepository.save(friendship);
                title = String.format("Great news! %s accepted your invite", receiver.getGamerTag());
                body = """
                        You’re officially connected!
                        
                        Your friend invite has been accepted, and you can now start chatting, teaming up, and sharing experiences together.
                        Explore new games, compete, cooperate, and build great moments on the platform.
                        
                        Have fun and game on!
                        
                        Kind regards,
                        The Arch Portal Team""";
                eventPublisher.publishEvent(new FriendShipCreatedEvent(receiver.getId().id(),receiver.getGamerTag(), sender.getId().id(), sender.getGamerTag()));
            }
            case DECLINE -> {
                receiver.removeFriendRequest(request);
                title = String.format("Your invite to %s was declined", receiver.getGamerTag());
                body = """
                        Thanks for reaching out and trying to connect.
                        
                        Unfortunately, your friend invite was declined this time. Don’t let that stop you—there are plenty of other players waiting to team up and explore new adventures with you.
                        
                        Keep playing, keep connecting, and new opportunities will come.
                        
                        Kind regards,
                        The Arch Portal Team""";
            }
        }
        eventPublisher.publishEvent(new AddNotificationEvent(sender.getId().id(), title, body, NotificationType.FRIEND_REQUEST));

        profileRepository.save(receiver);
    }

    public void cancelFriendRequest(ProfileId profileId, String gamerTag) {
        var sender = profileRepository.findById(profileId).orElseThrow(profileId::notFound);
        var receiver = profileRepository.findByGamerTag(gamerTag).orElseThrow(() -> new NotFoundException("Profile with gamertag " + gamerTag + " is not found."));

        var request = receiver.getIncomingFriendRequest(sender.getId());

        receiver.removeFriendRequest(request);

        profileRepository.save(receiver);
    }

    public void removeFriend(ProfileId profileId, String gamerTag) {
        var profile = profileRepository.findById(profileId).orElseThrow(profileId::notFound);
        var friend = profileRepository.findByGamerTag(gamerTag).orElseThrow(() -> new NotFoundException("Profile with gamertag " + gamerTag + " is not found."));

        var friendship = friendshipRepository.findBetween(profile.getId(), friend.getId()).orElseThrow(() -> new NotFoundException("You are not friends with " + gamerTag + "."));

        friendshipRepository.delete(friendship);
    }
}