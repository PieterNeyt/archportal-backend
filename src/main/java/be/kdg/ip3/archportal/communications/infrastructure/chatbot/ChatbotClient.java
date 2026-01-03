package be.kdg.ip3.archportal.communications.infrastructure.chatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Component
public class ChatbotClient {
    private final RestClient restClient;
    @Value("${team.number}")
    private int teamNumber;
    @Value("${model.provider}")
    private String modelProvider;
    @Value("${ai.api.key}")
    private String apiKey;

    public ChatbotClient(@Qualifier("chatbotApi") RestClient restClient) {
        this.restClient = restClient;
    }

    public String sendMessage(String question, String gameName, UUID userID) {
        var message = new ChatRequest(question, gameName, userID, teamNumber, modelProvider);
        log.info("Sending message to AI chatbot: {}", message);
        return restClient.post()
                .uri("")
                .header("X-API-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message)
                .retrieve()
                .body(String.class);
    }
}
