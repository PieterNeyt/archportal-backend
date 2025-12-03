package be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.events;

import be.kdg.ip3.archportal.communications.application.NotificationSettingsServices;
import be.kdg.ip3.archportal.communications.shared.CreateNotificationSettingsEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingsEventListener {
    private final NotificationSettingsServices service;

    public NotificationSettingsEventListener(NotificationSettingsServices service) {
        this.service = service;
    }

    @Async
    @ApplicationModuleListener
    public void createNotificationSettings(CreateNotificationSettingsEvent notificationSettings) {
        service.createNotificationSettings(notificationSettings.profileId());
    }
}
