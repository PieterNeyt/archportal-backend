package be.kdg.ip3.archportal.profiles.shared;

import java.util.UUID;

public record GrantPlatformBenefitEvent(UUID profileId, UUID benefitId, int cost)  {
}
