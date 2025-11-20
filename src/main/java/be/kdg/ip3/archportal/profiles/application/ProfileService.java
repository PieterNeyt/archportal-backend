package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.games.GamesApi;
import be.kdg.ip3.archportal.profiles.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import be.kdg.ip3.archportal.profiles.domain.library.Library;
import be.kdg.ip3.archportal.profiles.domain.library.LibraryRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final LibraryRepository libraryRepository;
    private final GamesApi gamesApi;

    public ProfileService(ProfileRepository profileRepository, LibraryRepository libraryRepository, GamesApi gamesApi) {
        this.profileRepository = profileRepository;
        this.libraryRepository = libraryRepository;
        this.gamesApi = gamesApi;
    }

    public CreateProfileCommand createProfile(CreateProfileCommand command) {
        var library = new Library(new ArrayList<>());
        libraryRepository.save(library);
        var profile = new Profile(
                command.id(),
                new ArrayList<>(),
                0,
                library.getId().id(),
                command.lastName(),
                command.icon(),
                command.gamerTag(),
                new ArrayList<>(),
                command.firstName()
        );

        profileRepository.save(profile);

        return CreateProfileCommand.fromDomain(profile, library);
    }

    public void acquireGames(AcquireGameCommand command) {

        List<UUID> nonExistentGames = gamesApi.validateGames(command.games());

        if (!nonExistentGames.isEmpty()) {
            throw new IllegalArgumentException(
                    "The following games do not exist: " + nonExistentGames
            );
        }

        var profile = profileRepository.findById(command.profileId());
        var library = libraryRepository.findById(profile.getLibraryId());
        for (UUID gameId : command.games()) {

            if (library.hasGame(gameId)) {
                throw new IllegalArgumentException(
                        "Game with id %s is already in the library".formatted(gameId)
                );
            }

            library.acquireGame(gameId);
        }

        libraryRepository.save(library);
    }
}