package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
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

    public ProfileService(ProfileRepository profileRepository, GamesApi gamesApi) {
        this.profileRepository = profileRepository;
        this.gamesApi = gamesApi;
    }

    public CreateProfileCommand createProfile(CreateProfileCommand command) {

        var profile = new Profile(
                command.id(),
                new ArrayList<>(),
                0,
                command.lastName(),
                command.icon(),
                command.gamerTag(),
                new ArrayList<>(),
                command.firstName(),
                new ArrayList<>()
        );

        profileRepository.save(profile);

        return CreateProfileCommand.fromDomain(profile);
    }

    public void acquireGames(AcquireGameCommand command) {
        List<UUID> nonExistentGames = gamesApi.validateGames(command.games());

        if (!nonExistentGames.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following games do not exist: " + nonExistentGames
            );
        }

        var profile = profileRepository.findById(command.profileId()).orElseThrow(command.profileId()::notFound);

        for (UUID gameId : command.games()) {
            if (profile.hasGame(gameId)) {
                throw new IllegalArgumentException(
                        "Game with id %s is already in the library".formatted(gameId)
                );
            }
            profile.acquireGame(gameId);
        }

        profileRepository.save(profile);
    }

    public Profile syncUser(Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        String firstName = token.getClaim("given_name");
        String lastName = token.getClaim("family_name");

        var profile = profileRepository.findById(profileId).orElse(Profile.createProfile(profileId, firstName, lastName));
        profileRepository.save(profile);
        return profile;
    }
}