package be.kdg.ip3.archportal.profiles.domain.profile;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository {
    void save(Profile profile);

    Optional<Profile> findById(ProfileId id);

    Optional<Profile> findByGamerTag(String username);

    List<Profile> findFromGamerTags(List<String> gamerTags);

    boolean existsById(ProfileId id);

    List<Profile> findAllFriends(ProfileId id);

    List<Profile> findFromIds(List<ProfileId> profileIds);

    List<Profile> findByIncomingRequestHasId(ProfileId senderId);
    List<UUID> findLibraryByPlayerId(ProfileId profileId);

}
