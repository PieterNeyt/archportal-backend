package be.kdg.ip3.archportal.profiles.domain.friendRequest;

public class InvalidFriendRequestException extends RuntimeException {
    public InvalidFriendRequestException(String message) {
        super(message);
    }
}
