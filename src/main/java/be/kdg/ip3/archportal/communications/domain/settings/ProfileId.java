package be.kdg.ip3.archportal.communications.domain.settings;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record ProfileId(UUID id) {
    public ProfileId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Profile [" + id + "] not found");
    }

    public static ProfileId create() {
        return new ProfileId(UUID.randomUUID());
    }
}
