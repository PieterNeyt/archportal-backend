package be.kdg.ip3.archportal.profiles.infrastructure.profile;

import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa.JpaProfileEntity;
import be.kdg.ip3.archportal.profiles.infrastructure.profile.jpa.JpaProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    @Override
    public Optional<Profile> findById(ProfileId id) {
        return jpaProfileRepository.findById(id.id()).map(JpaProfileEntity::toDomain);
    }
}
