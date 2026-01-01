package be.kdg.ip3.archportal.lobbies.api.dto;

import java.util.List;
import java.util.UUID;

public record PartyMembersDto(
        List<MemberDto> members,
        UUID startedLobbyId
) {}