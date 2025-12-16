package be.kdg.ip3.archportal.lobbies.infrastructure.party;

import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa.JpaPartyEntity;
import be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa.JpaPartyRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DbPartyRepository implements PartyRepository {
    private final JpaPartyRepository jpaPartyRepository;

    public DbPartyRepository(JpaPartyRepository jpaPartyRepository) {
        this.jpaPartyRepository = jpaPartyRepository;
    }

    @Override
    public void save(Party party) {
        this.jpaPartyRepository.save(JpaPartyEntity.fromDomain(party));
    }
}
