package be.kdg.ip3.archportal.lobbies.domain.id;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(GameSessionId id) {
        super("Session not found: " + id.id());
    }
}
