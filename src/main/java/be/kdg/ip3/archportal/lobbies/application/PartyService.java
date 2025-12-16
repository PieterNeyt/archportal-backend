package be.kdg.ip3.archportal.lobbies.application;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final ProfilesApi profilesApi;

    public PartyService(PartyRepository partyRepository, ProfilesApi profilesApi) {
        this.partyRepository = partyRepository;
        this.profilesApi = profilesApi;
    }

    public Party createParty(PlayerId hostId) {
        if (profilesApi.existsById(hostId.id()))
            throw hostId.notFound();
        
        var party = new Party(hostId);
        partyRepository.save(party);
        return party;
    }
}
