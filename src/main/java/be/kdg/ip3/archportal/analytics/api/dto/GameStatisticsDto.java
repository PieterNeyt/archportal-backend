package be.kdg.ip3.archportal.analytics.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GameStatisticsDto(
        UUID gameId,
        UUID profileId,
        Long totalPlayTimeMinutes, // Using Long to represent Duration in minutes for easier serialization
        LocalDateTime lastPlayedAt,
        List<AchievementDto> achievements,
        List<WinnerRecordDto> winnerRecords
) {
}
