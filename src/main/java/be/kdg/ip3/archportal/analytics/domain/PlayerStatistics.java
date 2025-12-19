package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
import be.kdg.ip3.archportal.profiles.domain.Library.Game;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@AggregateRoot
public class PlayerStatistics {
    private final PlayerId playerId;
    private List<GameStatistics> gameStatistics;
    private int TotalMinutesPlayed;
    private Date lastPlayed;

    public PlayerStatistics(PlayerId playerId, List<GameStatistics> gameStatistics, int totalMinutesPlayed, Date lastPlayed) {
        this.playerId = playerId;
        this.gameStatistics = gameStatistics;
        this.TotalMinutesPlayed = totalMinutesPlayed;
        this.lastPlayed = lastPlayed;
    }

    public PlayerStatistics(PlayerId playerId, int totalMinutesPlayed, Date lastPlayed) {
        this(playerId, new ArrayList<>(), totalMinutesPlayed, lastPlayed);
    }

    public GameStatistics findGameStatisticsById(GameStatisticsId gameId) {
        return gameStatistics.stream()
                .filter(game -> game.getGameStatisticsId().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "GameStatistics with id " + gameId + " not found"
                ));
    }

    public void addWinnerRecord(WinnerRecord result,GameStatisticsId gameId) {
        var gameStats = findGameStatisticsById(gameId);
        gameStats.addWinnerRecord(result);
    }

    public void addGameStatistics(GameStatisticsId gameId) {
        gameStatistics.add(new GameStatistics(gameId));
    }
}
