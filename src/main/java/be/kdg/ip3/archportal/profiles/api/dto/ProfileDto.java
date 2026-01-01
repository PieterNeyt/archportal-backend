package be.kdg.ip3.archportal.profiles.api.dto;

import be.kdg.ip3.archportal.profiles.domain.profile.Profile;
import be.kdg.ip3.archportal.profiles.domain.profile.Section;
import be.kdg.ip3.archportal.profiles.domain.profile.SectionType;
import be.kdg.ip3.archportal.profiles.domain.profile.Visibility;

import java.util.List;
import java.util.UUID;

public record ProfileDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String icon,
        String gamerTag,
        int platformPoints,
        List<GameDto> games,
        List<SectionDto> sections
) {
    public static ProfileDto from(Profile profile) {
        return new ProfileDto(
                profile.getId().id(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getIcon(),
                profile.getGamerTag(),
                profile.getPlatformPoints(),
                profile.getGames().stream()
                        .map(game -> new GameDto(game.getGameId(), game.isFavorite()))
                        .toList(),
                profile.getSections().stream()
                        .map(SectionDto::from)
                        .toList()
        );
    }

    public record SectionDto(SectionType type, Visibility visibility) {
        public static SectionDto from(Section section) {
            return new SectionDto(section.getType(), section.getVisibility());
        }
        public Section toDomain() {
            return new Section(this.type, this.visibility);
        }
    }

    public record GameDto(UUID gameId, boolean isFavorite) {}
}