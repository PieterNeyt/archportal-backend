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
import be.kdg.ip3.archportal.games.shared.GamesApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Slf4j
@Component
public class GameMessageHandler {
    private final AnalyticsService analyticsService;
    private final GamesApi gamesApi;

    public GameMessageHandler(AnalyticsService analyticsService, GamesApi gamesApi) {
        this.analyticsService = analyticsService;
        this.gamesApi = gamesApi;
    }

    @RabbitListener(queues = RabbitMQTopology.TTT_QUEUE_NAME)
    void onTTTGameResultMessage(TttGameResultMessage message) {
        log.info("Received message: {}", message);
        analyticsService.recordGameResult(
                new SessionId(message.sessionId()),
                message.winner(),
                message.timestamp()
        );
    }

    @RabbitListener(queues = RabbitMQTopology.CHECKERS_QUEUE_NAME)
    void onCheckersGameResultMessage(CheckersGameResultMessage msg) {
        analyticsService.recordGameResult(
                new SessionId(msg.sessionId()),
                msg.winner(),
                java.time.LocalDateTime.parse(msg.timestamp())
        );
    }

    @RabbitListener(queues = RabbitMQTopology.ACHIEVEMENT_QUEUE_NAME)
    void onAchievementUnlockedMessage(AchievementUnlockedMessage message) {

        var playerId = new PlayerId(message.playerId());
        var gameId = new GameId(message.gameId());
        var gameStatsId = new GameStatisticsId(gameId, playerId);

        analyticsService.grantAchievement(
                message.externalAchId(),
                playerId,
                gameStatsId
        );

    }

    @RabbitListener(queues = RabbitMQTopology.UNLOCKED_ACL_ACHIEVEMENT_QUEUE)
    void onUnlockedAclAchievementMessage(AchievementUnlockedMessage message) {

        log.info("ACL achievement message: gameId={}, playerId={}, externalAchId={}",
                message.gameId(), message.playerId(), message.externalAchId());

        var playerId = new PlayerId(message.playerId());
        var gameId = gamesApi.getGameIdByName("Chess");
        var gameStatsId = new GameStatisticsId(new GameId(gameId), playerId);

        analyticsService.grantAchievement(
                message.externalAchId(),
                playerId,
                gameStatsId
        );

    }


}
