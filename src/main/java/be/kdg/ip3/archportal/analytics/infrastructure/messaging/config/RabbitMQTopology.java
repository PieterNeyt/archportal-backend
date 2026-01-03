package be.kdg.ip3.archportal.analytics.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {


    // Receving
    public static final String TTT_EXCHANGE_NAME = "ttt-exchange";
    public static final String TTT_QUEUE_NAME = "ttt-queue";

    public static final String CHECKERS_EXCHANGE_NAME = "checkers-exchange";
    public static final String CHECKERS_QUEUE_NAME = "checkers-queue";

    public static final String ACHIEVEMENT_EXCHANGE_NAME = "achievement-exchange";
    public static final String ACHIEVEMENT_QUEUE_NAME = "achievement-queue";
    // ttt topology

    @Bean
    TopicExchange tttExchange() {
        return new TopicExchange(TTT_EXCHANGE_NAME);
    }

    @Bean
    Queue tttQueue() {
        return QueueBuilder.nonDurable(TTT_QUEUE_NAME).build();
    }

    @Bean
    Binding tttQueueToTttExchangeBinding() {
        return BindingBuilder.bind(tttQueue()).to(tttExchange()).with("ttt.game.*");
    }

    // Checkers
    @Bean
    TopicExchange checkersExchange() {
        return new TopicExchange(CHECKERS_EXCHANGE_NAME);
    }

    @Bean
    Queue checkersQueue() {
        return QueueBuilder.nonDurable(CHECKERS_QUEUE_NAME).build();
    }

    @Bean
    Binding checkersQueueToCheckersExchangeBinding() {
        return BindingBuilder.bind(checkersQueue()).to(checkersExchange()).with("checkers.game.*");
    }
    // Achievement topology
    @Bean
    TopicExchange achievementExchange() {
        return new TopicExchange(ACHIEVEMENT_EXCHANGE_NAME);
    }

    @Bean
    Queue achievementQueue() {
        return QueueBuilder.nonDurable(ACHIEVEMENT_QUEUE_NAME).build();
    }

    @Bean
    Binding achievementQueueToAchievementExchangeBinding() {
        return BindingBuilder.bind(achievementQueue()).to(achievementExchange()).with("*.achievement.unlock");
    }
}