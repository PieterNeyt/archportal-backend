package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.application.NotificationServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationServices notificationServices;
    public NotificationController(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }

    @PostMapping()
    public ResponseEntity<?> sendNotifactionExample() {
        notificationServices.sendNotification();
        return ResponseEntity.ok("");
    }
}
