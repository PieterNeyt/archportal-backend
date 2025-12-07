package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.NotificationDto;
import be.kdg.ip3.archportal.communications.application.NotificationServices;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationId;
import be.kdg.ip3.archportal.communications.domain.notification.RecieverId;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpContext;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/read/{id}")
    public ResponseEntity<NotificationId> readNotification(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        var recieverId = new RecieverId(UUID.fromString(jwt.getSubject()));
        var notificationId = new NotificationId(id);
        notificationServices.readNotification(recieverId,notificationId);

        return ResponseEntity.ok(notificationId);
    }
}
