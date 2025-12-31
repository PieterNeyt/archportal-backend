package be.kdg.ip3.archportal.communications.infrastructure.chatbot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ChatRequest(
        @JsonProperty("question")
        String question,
        @JsonProperty("game_name")
        String gameName,
        @JsonProperty("user_id")
        UUID userId,
        @JsonProperty("team_number")
        int teamNumber
) {
}
