package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequest;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestAlreadyExistsException;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.FriendRequestRepository;
import be.kdg.ip3.archportal.profiles.domain.friendRequest.InvalidFriendRequestException;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final ProfileRepository profileRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, ProfileRepository profileRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.profileRepository = profileRepository;
    }

    public FriendRequest createFriendRequest(ProfileId senderId, String gamerTag) {
        var receiver = profileRepository.findByGamerTag(gamerTag).orElseThrow(() -> new NotFoundException("Profile with gamertag " + gamerTag + " is not found."));
        var receiverId = receiver.getId();
        if (senderId.equals(receiverId))
            throw new InvalidFriendRequestException("Sender and receiver profiles cannot be the same profile.");

        if (!profileRepository.existsById(senderId))
            throw new NotFoundException("Sender or receiver profile does not exist.");

        if (friendRequestRepository.existsPending(senderId, receiverId) || friendRequestRepository.existsPending(receiverId, senderId))
            throw new FriendRequestAlreadyExistsException("A pending friend request already exists between these profiles.");

        var friendRequest = new FriendRequest(senderId, receiverId);
        friendRequestRepository.save(friendRequest);
        return friendRequest;
    }

    public List<Profile> findAllProfilesHasSendRequest(ProfileId profileId) {
        var requests = friendRequestRepository.findAllFromReceiver(profileId);
        return profileRepository.findFromIds(requests.stream().map(FriendRequest::getSenderId).toList());
    }
}
