package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class ProfileApiService implements ProfilesApi {
    private final ProfileRepository profileRepository;

    public ProfileApiService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        var profileId = new ProfileId(id);
        return profileRepository.existsById(profileId);
    }
}
