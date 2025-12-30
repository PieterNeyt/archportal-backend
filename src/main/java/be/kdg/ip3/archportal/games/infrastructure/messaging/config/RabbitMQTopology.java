package be.kdg.ip3.archportal.games.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {
    public static final String REGISTER_GAME_EXCHANGE = "register-exchange";
    public static final String REGISTER_GAME_QUEUE = "register-queue";

    @Bean
    TopicExchange registerExchange() {
        return new TopicExchange(REGISTER_GAME_EXCHANGE);
    }

    @Bean
    Queue registerQueue() {
        return QueueBuilder.nonDurable(REGISTER_GAME_QUEUE).build();
    }

    @Bean
    Binding registerQueueToRegisterExchangeBinding() {
        return BindingBuilder.bind(registerQueue()).to(registerExchange()).with("register.game.*");
    }
}
