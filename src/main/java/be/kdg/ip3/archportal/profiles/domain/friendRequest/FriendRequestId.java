package be.kdg.ip3.archportal.profiles.domain.friendRequest;

import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record FriendRequestId(UUID id) {
    public FriendRequestId {
        Assert.notNull(id, "id must not be null");
    }

    public static FriendRequestId create() {
        return new FriendRequestId(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        return new NotFoundException("Friend request [" + id + "] not found");
    }
}
