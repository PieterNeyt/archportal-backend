package be.kdg.ip3.archportal.lobbies.domain.session;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(GameSessionId id) {
        super("Session not found: " + id.id());
    }
}
