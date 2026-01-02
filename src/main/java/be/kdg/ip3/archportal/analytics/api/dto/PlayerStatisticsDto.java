package be.kdg.ip3.archportal.analytics.api.dto;

import java.util.Date;

public record PlayerStatisticsDto(long totalPlayTimeMinutes, Date lastPlayed) {
    public static PlayerStatisticsDto fromDomain(long totalPlayTimeMinutes, Date lastPlayed) {
        return new PlayerStatisticsDto(totalPlayTimeMinutes, lastPlayed);
    }
}
