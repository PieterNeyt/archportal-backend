package be.kdg.ip3.archportal.analytics.domain;


import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.ProfileId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
@Getter
public class GameStatistics {
    private final GameId gameId;
    private final ProfileId profileId;
    private Duration TotalPlayTimeMinutes;
    private LocalDateTime lastPlayedAt;
    private List<Achievements> achievements;
    private List<WinnerRecord> winnerRecords;

    public GameStatistics(GameId gameId, ProfileId profileId, Duration totalPlayTimeMinutes, LocalDateTime lastPlayedAt, List<Achievements> achievements, List<WinnerRecord> winnerRecords) {
        this.gameId = gameId;
        this.profileId = profileId;
        this.TotalPlayTimeMinutes = totalPlayTimeMinutes;
        this.lastPlayedAt = lastPlayedAt;
        this.achievements = achievements;
        this.winnerRecords = winnerRecords;
    }

    public GameStatistics(GameId gameId, ProfileId profileId) {
        this.gameId = gameId;
        this.profileId = profileId;
        this.TotalPlayTimeMinutes = Duration.ofMinutes(0);
        this.lastPlayedAt = LocalDateTime.of(0,  1, 1, 0, 0, 0 );
        this.achievements = new ArrayList<>();
        this.winnerRecords = new ArrayList<>();
    }

    public void addWinnerRecord(WinnerRecord winnerRecord) {
        this.winnerRecords.add(winnerRecord);
    }
}
