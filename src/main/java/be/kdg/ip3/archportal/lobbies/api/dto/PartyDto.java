package be.kdg.ip3.archportal.lobbies.api.dto;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;

import java.util.UUID;

public record PartyDto(String title, boolean hostIsYou, int maxMembers, UUID chatRoomId,UUID selectedGameId,UUID startedLobbyId) {
    public static PartyDto fromDomain(Party party, PlayerId memberId) {
        return new PartyDto(party.getTitle(), party.getHostId().equals(memberId), party.getMaxMembers(), party.getChatRoomId().id(), party.getSelectedGameId(),party.getStartedLobbyId());
    }
}
