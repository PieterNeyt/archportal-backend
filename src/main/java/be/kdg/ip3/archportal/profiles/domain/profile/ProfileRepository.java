package be.kdg.ip3.archportal.profiles.domain.profile;

import org.jmolecules.ddd.annotation.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository {
    void save(Profile profile);
    Optional<Profile> findById(ProfileId id);
}
