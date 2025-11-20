package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.profiles.application.command.AcquireGameCommand;
import be.kdg.ip3.archportal.profiles.application.command.CreateProfileCommand;
import be.kdg.ip3.archportal.profiles.domain.library.Library;
import be.kdg.ip3.archportal.profiles.domain.library.LibraryRepository;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final LibraryRepository libraryRepository;

    public ProfileService(ProfileRepository profileRepository, LibraryRepository libraryRepository) {
        this.profileRepository = profileRepository;
        this.libraryRepository = libraryRepository;
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

        var profile = profileRepository.findById(command.profileId());
        var library = libraryRepository.findById(profile.getLibraryId());
        command.games().forEach(library::AquireGame);
        libraryRepository.save(library);
    }
}