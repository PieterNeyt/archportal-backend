package be.kdg.ip3.archportal.lobbies.infrastructure.party.event;

import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.shared.LobbyEndedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PartyLobbyCleanupListener {

    private final PartyRepository partyRepository;

    public PartyLobbyCleanupListener(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Async
    @ApplicationModuleListener
    public void onLobbyEnded(LobbyEndedEvent event) {
        partyRepository.findByStartedLobbyId(event.lobbyId())
                .ifPresent(party -> {
                    party.startedLobbyId(null);
                    party.getAllMembers().forEach(party::toggleReady);
                    partyRepository.save(party);
                });
    }

}