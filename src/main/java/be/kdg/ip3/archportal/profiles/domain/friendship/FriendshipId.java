package be.kdg.ip3.archportal.profiles.domain.friendship;

import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record FriendshipId(UUID id) {
    public FriendshipId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Friendship [" + id + "] not found");
    }
}
