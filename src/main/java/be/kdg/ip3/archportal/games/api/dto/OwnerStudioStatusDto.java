package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudio;

import java.util.UUID;

public record OwnerStudioStatusDto(
        boolean hasStudio,
        UUID studioId,
        String name
) {
    public static OwnerStudioStatusDto noStudio() {
        return new OwnerStudioStatusDto(false, null, null);
    }

    public static OwnerStudioStatusDto from(GameStudio gameStudio) {
        return new OwnerStudioStatusDto(true, gameStudio.getId().id(), gameStudio.getName());
    }
}
