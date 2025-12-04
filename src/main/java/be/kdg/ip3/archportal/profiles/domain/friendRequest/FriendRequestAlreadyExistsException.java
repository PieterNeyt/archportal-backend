package be.kdg.ip3.archportal.profiles.domain.friendRequest;

public class FriendRequestAlreadyExistsException extends RuntimeException {
    public FriendRequestAlreadyExistsException(String message) {
        super(message);
    }
}
