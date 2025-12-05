package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.communications.shared.CreateNotificationSettingsEvent;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public CreateProfileCommand createProfile(CreateProfileCommand command) {

        var profile = new Profile(
                command.id(),
                new ArrayList<>(),
                0,
                command.lastName(),
                "",
                command.icon(),
                command.gamerTag(),
                new ArrayList<>(),
                command.firstName(),
                new ArrayList<>()
        );

        profileRepository.save(profile);

        return CreateProfileCommand.fromDomain(profile);
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
}