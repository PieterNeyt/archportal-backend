package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.application.PartyService;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/party")
public class PartyController {
    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping({"/", ""})
    public ResponseEntity<Void> createParty(@AuthenticationPrincipal Jwt token) {
        var hostId = new PlayerId(UUID.fromString(token.getSubject()));
        var party = partyService.createParty(hostId);
        var location = URI.create("/api/party/" + party.getId());
        return ResponseEntity.created(location).build();
    }
}
