package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.ChangeChannelTypeDto;
import be.kdg.ip3.archportal.communications.api.dto.NotificationSettingsDto;
import be.kdg.ip3.archportal.communications.application.NotificationSettingsServices;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notificationsettings")
public class NotificationSettingsController {
    private final NotificationSettingsServices service;
    public NotificationSettingsController(NotificationSettingsServices service) {
        this.service = service;
    }

    @PutMapping("/channeltype/add")
    public ResponseEntity<NotificationSettingsDto> addChannelType(@RequestBody ChangeChannelTypeDto addChannelTypeDto,
                                                                     @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var settingsResponse = service.addChannelType(addChannelTypeDto,profileId);

        return ResponseEntity.ok(settingsResponse);
    }

    @PutMapping("/channeltype/remove")
    public ResponseEntity<NotificationSettingsDto> removeChannelType(@RequestBody ChangeChannelTypeDto removeChannelTypeDto,
                                                                     @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var settingsResponse = service.removeChannelType(removeChannelTypeDto,profileId);

        return ResponseEntity.ok(settingsResponse);
    }
}
