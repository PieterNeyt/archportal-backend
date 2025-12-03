package be.kdg.ip3.archportal.profiles.domain.profile;

import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository {
    void save(Profile profile);

    Optional<Profile> findById(ProfileId id);

    boolean existsById(ProfileId id);

    List<Profile> findAllFriends(ProfileId id);
}
