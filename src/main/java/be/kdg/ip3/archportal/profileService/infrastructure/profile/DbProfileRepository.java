package be.kdg.ip3.archportal.profileService.infrastructure.profile;

import be.kdg.ip3.archportal.profileService.domain.profile.Profile;
import be.kdg.ip3.archportal.profileService.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.profileService.infrastructure.profile.jpa.JpaProfileEntity;
import be.kdg.ip3.archportal.profileService.infrastructure.profile.jpa.JpaProfileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbProfileRepository implements ProfileRepository {

    private final JpaProfileRepository jpaProfileRepository;

    public DbProfileRepository(JpaProfileRepository jpaProfileRepository) {
        this.jpaProfileRepository = jpaProfileRepository;
    }


    @Override
    public void save(Profile profile) {
        this.jpaProfileRepository.save(JpaProfileEntity.fromDomain(profile));
    }
}
