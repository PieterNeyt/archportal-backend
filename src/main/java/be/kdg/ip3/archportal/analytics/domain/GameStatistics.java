package be.kdg.ip3.archportal.analytics.domain;


import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AggregateRoot
@Getter
public class GameStatistics {
    private final GameId gameId;
    private Duration TotalPlayTimeMinutes;
    private LocalDateTime lastPlayedAt;
    private List<Achievements> achievements;
    private List<WinnerRecord> winnerRecords;

    public GameStatistics(GameId gameId, Duration totalPlayTimeMinutes, LocalDateTime lastPlayedAt, List<Achievements> achievements, List<WinnerRecord> winnerRecords) {
        this.gameId = gameId;
        this.TotalPlayTimeMinutes = totalPlayTimeMinutes;
        this.lastPlayedAt = lastPlayedAt;
        this.achievements = achievements;
        this.winnerRecords = winnerRecords;
    }

    public void addWinnerRecord(WinnerRecord winnerRecord) {
        this.winnerRecords.add(winnerRecord);
    }
}
