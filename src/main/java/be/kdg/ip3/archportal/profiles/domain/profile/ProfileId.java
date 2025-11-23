package be.kdg.ip3.archportal.profiles.domain.profile;

import be.kdg.ip3.archportal.profiles.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record ProfileId(UUID id) {
    public ProfileId {
        Assert.notNull(id, "id is null");
    }

    public static ProfileId create() {
        return new ProfileId(UUID.randomUUID());
    }

    public static ProfileId create(UUID profileId) {
        return new ProfileId(profileId);
    }

    public NotFoundException notFound() {
        return new NotFoundException("Profile [" + id + "] not found");
    }
}
