package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.application.NotificationSettingsServices;
import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificationsettings")
public class NotificationSettingsController {
    private final NotificationSettingsServices service;
    public NotificationSettingsController(NotificationSettingsServices service) {
        this.service = service;
    }

    @PutMapping("/channeltype/add")
    public ResponseEntity<List<ChannelType>> addChannelType(@RequestBody ChannelType addChannelType,
                                                                     @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var settingsResponse = service.addChannelType(addChannelType,profileId);

        return ResponseEntity.ok(settingsResponse);
    }

    @PutMapping("/channeltype/remove")
    public ResponseEntity<List<ChannelType>> removeChannelType(@RequestBody ChannelType removeChannelType,
                                                               @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var settingsResponse = service.removeChannelType(removeChannelType,profileId);

        return ResponseEntity.ok(settingsResponse);
    }
}
