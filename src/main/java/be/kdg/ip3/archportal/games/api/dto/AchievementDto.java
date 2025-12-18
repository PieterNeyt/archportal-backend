package be.kdg.ip3.archportal.games.api.dto;

import be.kdg.ip3.archportal.games.domain.achievement.Achievement;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AchievementDto(
        UUID id,

        String externalAchId,

        @NotNull
        @Size(min = 1, max = 100)
        String title,

        @NotNull
        @Size(min = 1, max = 255)
        String description,

        @NotNull
        @Size(min = 1, max = 255)
        String imageUrl
) {
    public static AchievementDto fromDomain(Achievement achievement) {
        return new AchievementDto(
                achievement.getId().id(),
                achievement.getExternalAchId().id(),
                achievement.getTitle(),
                achievement.getDescription(),
                achievement.getImageUrl()
        );
    }
}