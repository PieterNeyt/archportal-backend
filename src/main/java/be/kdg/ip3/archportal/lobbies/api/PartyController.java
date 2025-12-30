package be.kdg.ip3.archportal.lobbies.api;

import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
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
    public ResponseEntity<PartyMembersDto> getMembers(@AuthenticationPrincipal Jwt token) {
        var memberId = new PlayerId(UUID.fromString(token.getSubject()));
        var partyStatus = partyService.findMembers(memberId);
        return ResponseEntity.ok(partyStatus);
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

    @GetMapping("/eligible-games")
    public ResponseEntity<List<GlobalGameDto>> getEligibleGames(@AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        var games = partyService.getEligibleGames(playerId);
        return ResponseEntity.ok(games);
    }

    @PatchMapping("/select-game/{gameId}")
    public ResponseEntity<Void> selectGame(@PathVariable UUID gameId, @AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        partyService.selectGame(playerId, gameId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/selected-game")
    public ResponseEntity<GlobalGameDto> getSelectedGame(@AuthenticationPrincipal Jwt token) {
        var playerId = new PlayerId(UUID.fromString(token.getSubject()));
        var game = partyService.getSelectedGame(playerId);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.noContent().build();
    }

    @PatchMapping("/ready")
    public ResponseEntity<Void> toggleReady(@AuthenticationPrincipal Jwt token) {
        partyService.toggleReady(new PlayerId(UUID.fromString(token.getSubject())));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/start-game")
    public ResponseEntity<UUID> startGame(@AuthenticationPrincipal Jwt token) {
        var lobbyId = partyService.startPartyGame(new PlayerId(UUID.fromString(token.getSubject())));
        return ResponseEntity.ok(lobbyId);
    }
}
