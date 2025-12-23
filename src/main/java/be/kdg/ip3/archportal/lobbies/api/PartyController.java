package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.lobbies.api.dto.*;
import be.kdg.ip3.archportal.lobbies.application.PartyService;
import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyId;
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
    public ResponseEntity<Void> createParty(@Valid @RequestBody CreatePartyDto dto, @AuthenticationPrincipal Jwt token) {
        var hostId = new PlayerId(UUID.fromString(token.getSubject()));
        var party = partyService.createParty(hostId, dto.title(), dto.maxMembers());
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
        return ResponseEntity.ok(members);
    }

    @PostMapping("/invite")
    public ResponseEntity<Void> sendPartyInvite(@Valid @RequestBody SendInviteDto dto, @AuthenticationPrincipal Jwt token) {
        var memberId = new PlayerId(UUID.fromString(token.getSubject()));
        var invite = partyService.sendInvite(memberId, dto.gamerTag());
        var location = URI.create("/api/party/invite" + invite.getId().id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/invite")
    public ResponseEntity<List<PartyInviteDto>> getPartyInvites(@AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        var parties = partyService.getPartiesWhereUserIsInvited(playerId);
        return ResponseEntity.ok(parties);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<PlayerDto>> getInvitableFriends(@AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        var friends = partyService.getInvitableFriends(playerId);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{id}/accept")
    public ResponseEntity<Void> acceptPartyInvite(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        partyService.acceptPartyInvite(playerId, new PartyId(id));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/decline")
    public ResponseEntity<Void> declinePartyInvite(@PathVariable UUID id, @AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        partyService.declinePartyInvite(playerId, new PartyId(id));
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/leave")
    public ResponseEntity<Void> leaveParty(@AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        partyService.leaveParty(playerId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/kick/{gamertag}")
    public ResponseEntity<Void> kickFromParty(@PathVariable String gamertag, @AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        partyService.kickFromParty(playerId, gamertag);
        return ResponseEntity.noContent().build();
    }
}
