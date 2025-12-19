package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.api.dto.MemberDto;
import be.kdg.ip3.archportal.lobbies.api.dto.PartyDto;
import be.kdg.ip3.archportal.lobbies.api.dto.SendInviteDto;
import be.kdg.ip3.archportal.lobbies.application.PartyService;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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
        var location = URI.create("/api/party/" + party.getId().id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping({"/", ""})
    public ResponseEntity<PartyDto> getParty(@AuthenticationPrincipal Jwt token) {
        var memberId = new PlayerId(UUID.fromString(token.getSubject()));
        var party = partyService.findPartyByMemberId(memberId);
        return ResponseEntity.ok(PartyDto.fromDomain(party, memberId));
    }

    @GetMapping({"/members"})
    public ResponseEntity<List<MemberDto>> getMembers(@AuthenticationPrincipal Jwt token) {
        var memberId = new PlayerId(UUID.fromString(token.getSubject()));
        var members = partyService.findMembers(memberId);
        return ResponseEntity.ok(members.stream().map(MemberDto::from).toList());
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> sendPartyInvite(@Valid @RequestBody SendInviteDto dto, @AuthenticationPrincipal Jwt token) {
        var memberId = new PlayerId(UUID.fromString(token.getSubject()));
        var invite = partyService.sendInvite(memberId, dto.gamerTag());
        var location = URI.create("/api/party/invite" + invite.getId().id());
        return ResponseEntity.created(location).build();
    }
}
