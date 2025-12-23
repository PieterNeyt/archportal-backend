package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.lobbies.domain.party.Party;

import java.util.UUID;

public record PartyInviteDto(UUID partyId, String title, String gamerTag, int maxMembers, int memberCount) {
    public static PartyInviteDto from(Party party, String senderGamerTag) {
        return new PartyInviteDto(
                party.getId().id(),
                party.getTitle(),
                senderGamerTag,
                party.getMaxMembers(),
                party.getMembers().size() + 1
        );
    }
}
