package be.kdg.ip3.archportal.games.shared;

import be.kdg.ip3.archportal.games.domain.achievement.Achievement;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementDto(
        UUID achievementId,
        String title,
        String description,
        String imageUrl,
        LocalDateTime unlockedAt
) {
    public static AchievementDto fromDomain(Achievement achievement, LocalDateTime unlockedAt) {
        return new AchievementDto(
                achievement.getId().id(),
                achievement.getTitle(),
                achievement.getDescription(),
                achievement.getImageUrl(),
                unlockedAt
        );
    }
}