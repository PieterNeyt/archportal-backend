package be.kdg.ip3.archportal.games.infrastructure.messaging.receiver;

import be.kdg.ip3.archportal.games.application.GameService;
import be.kdg.ip3.archportal.games.application.command.GameCommand;
import be.kdg.ip3.archportal.games.infrastructure.messaging.config.RabbitMQTopology;
import be.kdg.ip3.archportal.games.infrastructure.messaging.config.RegisterGameMessage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegisterMessageHandler {
    private final GameService gameService;

    public RegisterMessageHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @RabbitListener(queues = RabbitMQTopology.REGISTER_GAME_QUEUE, containerFactory = "simpleRabbitListenerContainerFactory")
    void onRegisterGameMessage(@Valid RegisterGameMessage message) {
        log.info("Received message: {}", message);
        var game = gameService.createGameFromMessage(GameCommand.fromMessage(message));
        if (message.achievements() != null) {
            message.achievements().forEach(a -> {
                gameService.addAchievementFromExternalSystem(a, game.getId());
            });
        }
    }

    @RabbitListener(
            queues = RabbitMQTopology.REGISTER_ACL_GAME_QUEUE,
            containerFactory = "simpleRabbitListenerContainerFactory"
    )
    void onRegisterAclGameMessage(@Valid RegisterGameMessage message) {
        log.info("Received register game message from acl: {}", message);
        var game = gameService.createGameFromMessage(GameCommand.fromMessage(message));
        if (message.achievements() != null) {
            message.achievements().forEach(a -> {
                gameService.addAchievementFromExternalSystem(a, game.getId());
            });
        }
    }


}
