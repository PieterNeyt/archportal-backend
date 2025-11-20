package be.kdg.ip3.archportal.profileService.domain.profile;

import org.springframework.util.Assert;

import java.util.UUID;

public record ProfileId(UUID id) {
    public ProfileId {
        Assert.notNull(id, "id is null");
    }
    public static ProfileId create() {
        return new ProfileId(UUID.randomUUID());
    }

}
