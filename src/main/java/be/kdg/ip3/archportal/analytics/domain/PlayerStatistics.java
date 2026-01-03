package be.kdg.ip3.archportal.analytics.domain;

import be.kdg.ip3.archportal.analytics.domain.records.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@AggregateRoot
public class PlayerStatistics {
    private final PlayerId playerId;
    private List<GameStatistics> gameStatistics;
    private Duration totalTimePlayed;
    private Date lastPlayed;

    public PlayerStatistics(PlayerId playerId, List<GameStatistics> gameStatistics, Duration totalMinutesPlayed, Date lastPlayed) {
        this.playerId = playerId;
        this.gameStatistics = new ArrayList<>(gameStatistics);;
        this.totalTimePlayed = totalMinutesPlayed;
        this.lastPlayed = lastPlayed;
    }

    public PlayerStatistics(PlayerId playerId) {
        this(playerId, new ArrayList<>(), Duration.ZERO, null);
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

    public Map<UUID, LocalDateTime> getAchievementIds(GameStatisticsId gameStatsId) {
        var gameStats = findGameStatisticsById(gameStatsId);

        return gameStats.getAchievementIds();
    }

    public void update(GameStatisticsId gameStatisticsId, LocalDateTime startTime, LocalDateTime endTime) {
        this.lastPlayed = Date.from(Instant.now());
         var minutesPlayed = Duration.between(startTime, endTime).toMinutes();

        this.totalTimePlayed = totalTimePlayed.plusMinutes(minutesPlayed);

        var gameStats = findGameStatisticsById(gameStatisticsId);
        gameStats.update(minutesPlayed);
    }

    public Map<UUID, LocalDateTime> getAllAchievementIds() {
        var achievements = new HashMap<UUID, LocalDateTime>();
        for (var gameStat : gameStatistics) {
            achievements.putAll(gameStat.getAchievementIds());
        }
        return achievements;
    }

    public List<UUID> getAllGameIds() {
        var gameIds = new ArrayList<UUID>();
        for (var gameStat : gameStatistics) {
            gameIds.add(gameStat.getGameStatisticsId().gameId().id());
        }
        return gameIds;
    }
}
