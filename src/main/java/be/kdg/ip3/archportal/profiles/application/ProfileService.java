package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.communications.shared.CreateNotificationSettingsEvent;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.*;
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

    public ProfileService(ProfileRepository profileRepository, GamesApi gamesApi, ApplicationEventPublisher eventPublisher) {
        this.profileRepository = profileRepository;
        this.gamesApi = gamesApi;
        this.eventPublisher = eventPublisher;
    }

    public Profile syncUser(Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        String firstName = token.getClaim("given_name");
        String lastName = token.getClaim("family_name");
        String gamerTag = token.getClaim("preferred_username");
        String email = token.getClaim("email");

        var profile = profileRepository.findById(profileId).orElseGet(() -> {
            Profile newProfile = Profile.createProfile(profileId, firstName, lastName, gamerTag, email);
            eventPublisher.publishEvent(new CreateNotificationSettingsEvent(profileId.id()));
            return newProfile;
        });

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
        var receiverId = receiver.getId();
        if (senderId.equals(receiverId))
            throw new InvalidFriendRequestException("Sender and receiver profiles cannot be the same profile.");

        var sender = profileRepository.findById(senderId).orElseThrow(() -> new NotFoundException("Sender with id " + senderId + " is not found."));
        if (sender.getFriends().contains(receiver.getId()))
            throw new AlreadyFriendException(gamerTag);

        boolean exists = receiver.getIncomingFriendRequests().stream()
                .anyMatch(r -> r.senderId().equals(senderId)) ||
                sender.getIncomingFriendRequests().stream()
                        .anyMatch(r -> r.senderId().equals(receiverId));
        if (exists)
            throw new FriendRequestAlreadyExistsException("A pending friend request already exists between these profiles.");

        var friendRequest = new FriendRequest(senderId);
        receiver.addIncomingFriendRequest(friendRequest);

        profileRepository.save(sender);
        profileRepository.save(receiver);
        return friendRequest;
    }

    public List<Profile> findAllProfilesIncomingRequests(ProfileId receiverId) {
        var receiver = profileRepository.findById(receiverId).orElseThrow(receiverId::notFound);
        var senderIds = receiver.getIncomingFriendRequests().stream().map(FriendRequest::senderId).toList();
        return profileRepository.findFromIds(senderIds);
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

        switch (action) {
            case ACCEPT -> receiver.acceptFriendRequest(request);
            case DECLINE -> receiver.declineFriendRequest(request);
        }

        profileRepository.save(receiver);
        profileRepository.save(sender);
    }
}