package be.kdg.ip3.archportal.analytics.api.dto;

import be.kdg.ip3.archportal.analytics.domain.GameStatistics;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GameStatisticsDto(
        UUID gameId,
        UUID profileId,
        Long totalPlayTimeMinutes,
        LocalDateTime lastPlayedAt,
        List<AchievementDto> achievements,
        List<WinnerRecordDto> winnerRecords
) {
    public static GameStatisticsDto fromDomain(GameStatistics gameStatistics) {
        return new GameStatisticsDto(
                gameStatistics.getGameStatisticsId().gameId().id(),
                gameStatistics.getGameStatisticsId().playerId().id(),
                gameStatistics.getTotalPlayTimeMinutes().toMinutes(),
                gameStatistics.getLastPlayedAt(),
                gameStatistics.getAchievements().stream()
                        .map(achievement -> new AchievementDto(achievement.getAchievementId().id(), achievement.getTimeUnlocked()))
                        .toList(),
                gameStatistics.getWinnerRecords().stream()
                        .map(winnerRecord -> new WinnerRecordDto(
                                winnerRecord.PlayedAt(),
                                winnerRecord.Winner(),
                                winnerRecord.SessionId()
                        ))
                        .toList()
        );
    }
}
