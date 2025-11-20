package be.kdg.ip3.archportal.profileService.domain.library;

import org.springframework.util.Assert;

import java.util.UUID;

public record LibraryId(UUID id) {
    public LibraryId {
        Assert.notNull(id, "id is null");
    }
    public static LibraryId create() {
        return new LibraryId(UUID.randomUUID());
    }

}
