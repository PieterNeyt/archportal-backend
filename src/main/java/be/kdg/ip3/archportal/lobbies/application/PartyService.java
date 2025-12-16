package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.shared.CreatePartyEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProfilesApi profilesApi;
    private final ApplicationEventPublisher publisher;

    public PartyService(PartyRepository partyRepository, ProfilesApi profilesApi, ApplicationEventPublisher publisher) {
        this.partyRepository = partyRepository;
        this.profilesApi = profilesApi;
        this.publisher = publisher;
    }

    public Party createParty(PlayerId hostId) {
        if (profilesApi.existsById(hostId.id()))
            throw hostId.notFound();

        var party = new Party(hostId);
        partyRepository.save(party);
        publisher.publishEvent(new CreatePartyEvent(hostId.id()));
        return party;
    }
}
