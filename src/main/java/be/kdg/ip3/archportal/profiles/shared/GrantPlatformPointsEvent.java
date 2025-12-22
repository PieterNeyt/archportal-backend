package be.kdg.ip3.archportal.profiles.shared;

import java.util.UUID;

public record GrantPlatformPointsEvent(UUID profileId, int points) {
}
