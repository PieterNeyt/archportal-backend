package be.kdg.ip3.archportal.profiles.application.command;

import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformBenefitEvent;

import java.util.UUID;

public record AcquirePlatformBenefitCommand(
        ProfileId profileId,
        UUID benefitId,
        int cost
) {
    public static AcquirePlatformBenefitCommand fromDto(GrantPlatformBenefitEvent dto) {
        return new AcquirePlatformBenefitCommand(
                new ProfileId(dto.profileId()),
                dto.benefitId(),
                dto.cost()
        );
    }
}