package be.kdg.ip3.archportal.analytics.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementDto(UUID achievementId, LocalDateTime timeUnlocked) {
}
