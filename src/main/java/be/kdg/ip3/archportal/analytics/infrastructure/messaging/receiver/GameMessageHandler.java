package be.kdg.ip3.archportal.analytics.infrastructure.messaging.receiver;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.GameStatisticsId;
import be.kdg.ip3.archportal.analytics.domain.records.PlayerId;
import be.kdg.ip3.archportal.analytics.domain.records.SessionId;
import be.kdg.ip3.archportal.analytics.infrastructure.messaging.config.AchievementUnlockedMessage;
import be.kdg.ip3.archportal.analytics.infrastructure.messaging.config.CheckersGameResultMessage;
import be.kdg.ip3.archportal.analytics.infrastructure.messaging.config.RabbitMQTopology;
import be.kdg.ip3.archportal.analytics.infrastructure.messaging.config.TttGameResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class GameMessageHandler {
    private final AnalyticsService analyticsService;

    public GameMessageHandler(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_QUEUE_NAME)
    void onTTTGameResultMessage(TttGameResultMessage message) {
        log.info("Received message: {}", message);
        analyticsService.recordGameResult(
                new SessionId(message.sessionId()) ,
                message.winner(),
                message.timestamp()
        );
    }
    @RabbitListener(queues = RabbitMQTopology.CHECKERS_QUEUE_NAME)
    void onCheckersGameResultMessage(CheckersGameResultMessage message) {
        log.info("Received Checkers message: {}", message);
        analyticsService.recordGameResult(
                new SessionId(message.sessionId()),
                message.winner(),
                message.timestamp()
        );
    }
    @RabbitListener(queues = RabbitMQTopology.ACHIEVEMENT_QUEUE_NAME)
    void onAchievementUnlockedMessage(AchievementUnlockedMessage message) {
        log.info("Received Achievement unlock message: {}", message);
        var playerId = new PlayerId(message.playerId());
        var gameId = new GameId(message.gameId());
        var gameStatsId = new GameStatisticsId(gameId, playerId);

        analyticsService.grantAchievement(
                message.externalAchId(),
                playerId,
                gameStatsId
        );
    }
}
