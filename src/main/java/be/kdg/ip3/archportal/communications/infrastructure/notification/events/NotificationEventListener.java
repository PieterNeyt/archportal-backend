package be.kdg.ip3.archportal.communications.infrastructure.notification.events;

import be.kdg.ip3.archportal.communications.application.NotificationServices;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {
    private final NotificationServices service;

    public NotificationEventListener(NotificationServices service) {
        this.service = service;
    }

    @Async
    @ApplicationModuleListener
    public void addNotification(AddNotificationEvent newNotificationEvent) {
        service.addNotification(newNotificationEvent);
    }
}
