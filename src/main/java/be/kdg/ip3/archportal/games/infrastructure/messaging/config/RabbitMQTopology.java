package be.kdg.ip3.archportal.games.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("RegisterGameRabbitMQTopology")
public class RabbitMQTopology {
    public static final String REGISTER_GAME_EXCHANGE = "register-exchange";
    public static final String REGISTER_GAME_QUEUE = "register-queue";

    public static final String REGISTER_GAME_DLX = "register-exchange.dlx";
    public static final String REGISTER_GAME_DLQ = "register-queue.dlq";

    public static final String PLATFORM_EXCHANGE = "platformExchange";
    public static final String REGISTER_ACL_GAME_QUEUE = "archportal.games.register.queue";



    @Bean
    TopicExchange registerExchange() {
        return new TopicExchange(REGISTER_GAME_EXCHANGE);
    }

    @Bean
    Queue registerQueue() {
        return QueueBuilder.nonDurable(REGISTER_GAME_QUEUE)
                .withArgument("x-dead-letter-exchange", REGISTER_GAME_DLX)
                .withArgument("x-dead-letter-routing-key", "register.game.failed")
                .build();
    }

    @Bean
    Binding registerQueueToRegisterExchangeBinding() {
        return BindingBuilder.bind(registerQueue()).to(registerExchange()).with("register.game.*");
    }

    @Bean
    TopicExchange registerGameDlx() {
        return new TopicExchange(REGISTER_GAME_DLX);
    }

    @Bean
    Queue registerGameDlq() {
        return QueueBuilder.nonDurable(REGISTER_GAME_DLQ).build();
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder
                .bind(registerGameDlq())
                .to(registerGameDlx())
                .with("register.game.failed");
    }



    @Bean
    Queue registerGameQueue() {
        return QueueBuilder.nonDurable(REGISTER_ACL_GAME_QUEUE).build();
    }

    @Bean
    Binding registerGameBinding(Queue registerGameQueue, TopicExchange platformExchange) {
        return BindingBuilder.bind(registerGameQueue)
                .to(platformExchange)
                .with("platform.game.registered");
    }
}
