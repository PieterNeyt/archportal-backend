package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.AchievementId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.domain.records.WinnerRecord;
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
        this.gameStatistics = new ArrayList<>(gameStatistics);;
        this.TotalMinutesPlayed = totalMinutesPlayed;
        this.lastPlayed = lastPlayed;
    }

    public PlayerStatistics(PlayerId playerId, int totalMinutesPlayed, Date lastPlayed) {
        this(playerId, new ArrayList<>(), totalMinutesPlayed, lastPlayed);
    }

    public GameStatistics findGameStatisticsById(GameStatisticsId gameStatsId) {
        return gameStatistics.stream()
                .filter(game -> game.getGameStatisticsId().equals(gameStatsId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "GameStatistics with id " + gameStatsId + " not found"
                ));
    }

    public void addWinnerRecord(WinnerRecord result,GameStatisticsId gameId) {
        var gameStats = findGameStatisticsById(gameId);
        gameStats.addWinnerRecord(result);
    }

    public void addGameStatistics(GameStatisticsId gameStatsId) {
        gameStatistics.add(new GameStatistics(gameStatsId));
    }

    public void addAchievementToGame(AchievementId achievementId, GameStatisticsId gameStatsId) {
        var gameStats = findGameStatisticsById(gameStatsId);

        gameStats.addAchievement(achievementId);

    }
}
