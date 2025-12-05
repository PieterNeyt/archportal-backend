package be.kdg.ip3.archportal.profiles.domain.friendRequest;

public class AlreadyFriendException extends RuntimeException {
    public AlreadyFriendException(String receiverGamerTag) {
        super("You are already friends with " + receiverGamerTag);
    }
}
