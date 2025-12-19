package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.lobbies.domain.party.Party;

public record PartyInviteDto(String title, String senderGamerTag, int maxMembers, int memberCount) {
    public static PartyInviteDto from(Party party, String senderGamerTag) {
        return new PartyInviteDto(
                party.getTitle(),
                senderGamerTag,
                party.getMaxMembers(),
                party.getInvites().size()
        );
    }
}
