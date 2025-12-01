package be.kdg.ip3.archportal.communications.domain.settings;

import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record SettingId(UUID id) {
    public SettingId {
        Assert.notNull(id, "id is null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Setting [" + id + "] not found");
    }

    public static SettingId create() {
        return new SettingId(UUID.randomUUID());
    }
}
