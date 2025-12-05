package be.kdg.ip3.archportal.analytics.infrastructure.messaging.receiver;

import be.kdg.ip3.archportal.analytics.application.AnalyticsService;
import be.kdg.ip3.archportal.analytics.domain.records.GameId;
import be.kdg.ip3.archportal.analytics.domain.records.ProfileId;
import be.kdg.ip3.archportal.analytics.domain.records.SessionId;
import be.kdg.ip3.archportal.analytics.infrastructure.messaging.config.RabbitMQTopology;
import be.kdg.ip3.archportal.analytics.infrastructure.messaging.config.TttGameResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
                new GameId(message.gameId()),
                message.winner(),
                message.timestamp()
        );

    }
}
