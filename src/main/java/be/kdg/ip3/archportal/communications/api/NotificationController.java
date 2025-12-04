package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.NotificationDto;
import be.kdg.ip3.archportal.communications.application.NotificationServices;
import be.kdg.ip3.archportal.communications.domain.notification.RecieverId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationServices notificationServices;
    public NotificationController(NotificationServices notificationServices) {
        this.notificationServices = notificationServices;
    }

    @GetMapping()
    public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal Jwt jwt) {
        var recieverId = new RecieverId(UUID.fromString(jwt.getSubject()));
        var notificaitons = notificationServices.getNotifications(recieverId);

        return ResponseEntity.ok(
                notificaitons
                .stream()
                .map(NotificationDto::fromDomain)
                .toList());
    }

    @GetMapping("/first/5")
    public ResponseEntity<List<NotificationDto>> getFirst5Notifications(@AuthenticationPrincipal Jwt jwt) {
        var recieverId = new RecieverId(UUID.fromString(jwt.getSubject()));
        var notificaitons = notificationServices.getFirst5Notifications(recieverId);

        return ResponseEntity.ok(
                notificaitons
                        .stream()
                        .map(NotificationDto::fromDomain)
                        .toList());
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalNotifications(@AuthenticationPrincipal Jwt jwt) {
        var recieverId = new RecieverId(UUID.fromString(jwt.getSubject()));
        var totalNotifications = notificationServices.getTotalNotifications(recieverId);

        return ResponseEntity.ok(totalNotifications);
    }
}
