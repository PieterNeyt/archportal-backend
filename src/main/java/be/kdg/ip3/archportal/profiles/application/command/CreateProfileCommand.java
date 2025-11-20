package be.kdg.ip3.archportal.profiles.application.command;

import be.kdg.ip3.archportal.profiles.api.dto.CreateProfileDto;
import be.kdg.ip3.archportal.profiles.domain.library.Library;
import be.kdg.ip3.archportal.profiles.domain.profile.Profile;

import java.util.UUID;

public record CreateProfileCommand(
        UUID id,
        String firstName,
        String lastName,
        String gamerTag,
        String icon,
        UUID libraryId
) {
    public static CreateProfileCommand fromDto(CreateProfileDto dto) {
        return new CreateProfileCommand(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.gamerTag(),
                dto.icon(),
                dto.libraryId()
        );
    }

    public static CreateProfileCommand fromDomain(Profile profile, Library library) {
        return new CreateProfileCommand(
                profile.getId().id(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getGamerTag(),
                profile.getIcon(),
                library.getId().id()
        );
    }
}