package be.kdg.ip3.archportal.profileService.domain.profile;

import org.jmolecules.ddd.annotation.Repository;

import java.util.UUID;

@Repository
public interface ProfileRepository {
    void save(Profile profile);
    Profile findById(UUID id);
}
