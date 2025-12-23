package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.lobbies.shared.SessionEndedEvent;

import java.time.LocalDateTime;

public record GameStatsCommand(GameId gameId, PlayerId playerId, LocalDateTime startTime, LocalDateTime endTime) {
    public static GameStatsCommand fromEvent(SessionEndedEvent event){
        var playerId = new PlayerId(event.playerId());
        var gameId = new GameId(event.gameId());

        return new GameStatsCommand(
                gameId,
                playerId,
                event.startTime(),
                event.endTime()
        );
    }
}
