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

    public ChatbotClient(@Qualifier("chatbotApi") RestClient restClient) {
        this.restClient = restClient;
    }

    public String sendMessage(String question, String gameName, UUID userID) {
        var message = new ChatRequest(question, gameName, userID, teamNumber);
        log.info("Sending message to AI chatbot: {}", message);
        return restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(message)
                .retrieve()
                .body(String.class);
    }
}
