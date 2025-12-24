package be.kdg.ip3.archportal.profiles.infrastructure.profile.event;

import be.kdg.ip3.archportal.profiles.application.ProfileService;
import be.kdg.ip3.archportal.profiles.application.command.AcquirePlatformBenefitCommand;
import be.kdg.ip3.archportal.profiles.application.command.AcquirePlatformPointsCommand;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformBenefitEvent;
import be.kdg.ip3.archportal.profiles.shared.GrantPlatformPointsEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProfileEventListener {

    private final ProfileService profileService;
    public ProfileEventListener( ProfileService profileService) {
        this.profileService = profileService;
    }

    @Async
    @ApplicationModuleListener
    public void onGrantPlatformPoints(GrantPlatformPointsEvent event) {
        profileService.aquirePlatformPoints(
                AcquirePlatformPointsCommand.fromDto(event)
        );
    }
    @Async
    @ApplicationModuleListener
    public void onGrantPlatformBenefit(GrantPlatformBenefitEvent event) {
        profileService.aquirePlatformBenefit(AcquirePlatformBenefitCommand.fromDto(event));
    } 
}
