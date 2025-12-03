package be.kdg.ip3.archportal.communications.api;

import be.kdg.ip3.archportal.communications.api.dto.AddChannelTypeDto;
import be.kdg.ip3.archportal.communications.api.dto.NotificationSettingsDto;
import be.kdg.ip3.archportal.communications.application.NotificationServices;
import be.kdg.ip3.archportal.communications.application.NotificationSettingsServices;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
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

    @PutMapping()
    public ResponseEntity<NotificationSettingsDto> changeChannelType(@RequestBody AddChannelTypeDto addChannelTypeDto,
                                                                     @AuthenticationPrincipal Jwt token) {
        var profileId = new ProfileId(UUID.fromString(token.getSubject()));
        var settingsResponse = service.changeChannelType(addChannelTypeDto,profileId);

        return ResponseEntity.ok(settingsResponse);
    }
}
