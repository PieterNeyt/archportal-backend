package be.kdg.ip3.archportal.communications.infrastructure.chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ChatbotConfig {
    @Bean("chatbotApi")
    RestClient chatbotRestTemplate(@Value("${chatbot.api.url}") final String url) {
        return RestClient.create(url);
    }
}
