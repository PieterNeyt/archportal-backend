package be.kdg.ip3.archportal.games.application.command;

import be.kdg.ip3.archportal.games.api.dto.AchievementDto;
import be.kdg.ip3.archportal.games.domain.achievement.Achievement;
import be.kdg.ip3.archportal.games.domain.achievement.AchievementId;
import be.kdg.ip3.archportal.games.domain.achievement.ExternalAchId;

import java.util.UUID;

public record AchievementCommand(
        UUID id,
        String externalAchId,
        String title,
        String description,
        String imageUrl
) {
    public static AchievementCommand fromDto(AchievementDto achievementDto) {
        return new AchievementCommand(
                achievementDto.id(),
                achievementDto.externalAchId(),
                achievementDto.title(),
                achievementDto.description(),
                achievementDto.imageUrl()
        );
    }

    public Achievement toDomain() {
        AchievementId achievementId;
        if (id == null) {
            achievementId = AchievementId.create();
        } else {
            achievementId = new AchievementId(id);
        }

        return new Achievement(
                achievementId,
                title,
                description,
                imageUrl,
                new ExternalAchId(externalAchId)
        );
    }
}