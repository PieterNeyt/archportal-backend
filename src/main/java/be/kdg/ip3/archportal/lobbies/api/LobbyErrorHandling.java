package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.domain.id.SessionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class LobbyErrorHandling {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }


    public record ErrorResponse(String message) {
    }
}

