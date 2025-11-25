package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.profiles.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class ProfileService implements ProfilesApi {
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


    @Override
    public void checkAlreadyOwnsGames(UUID profileId, List<UUID> games) {
        var profile = profileRepository.findById(new  ProfileId(profileId));

        List<UUID> alreadyOwned = games.stream()
                .filter(profile::hasGame)
                .toList();

        if (!alreadyOwned.isEmpty()) {
            throw new IllegalArgumentException(
                    "Profile %s already owns the following games: %s"
                            .formatted(profileId, alreadyOwned)
            );
        }
    }

    @Override
    public void checkAlreadyOwnsGame(UUID profileId, UUID gameId) {
        var profile = profileRepository.findById(new ProfileId(profileId));
        if (profile.hasGame(gameId)) {
            throw new IllegalArgumentException(
                    "Profile %s already owns the games: %s"
                            .formatted(profileId, gameId)
            );
        }
    }


    public void acquireGames(AcquireGameCommand command) {
        List<UUID> nonExistentGames = gamesApi.validateGames(command.games());
        if (!nonExistentGames.isEmpty()) {
            throw new IllegalArgumentException("The following games do not exist: " + nonExistentGames);
        }

        checkAlreadyOwnsGames(command.profileId(), command.games());
        var profile = profileRepository.findById(new ProfileId( command.profileId())).orElseThrow(command.profileId()::notFound);;

        command.games().forEach(profile::acquireGame);
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

    public List<GlobalGameDto> getLibrary(ProfileId profileId) {
        var profile = profileRepository.findById(profileId);
        var gameIds = profile.getLibrary();

        return gamesApi.getGamesByIds(gameIds);
    }

    @Override
    public void addGamesToLibrary(UUID profileId, List<UUID> games) {
        acquireGames(new AcquireGameCommand(profileId, games));
    }
}