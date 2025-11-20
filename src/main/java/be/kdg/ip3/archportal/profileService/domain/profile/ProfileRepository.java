package be.kdg.ip3.archportal.profileService.domain.profile;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface ProfileRepository {
    void save(Profile profile);
}
