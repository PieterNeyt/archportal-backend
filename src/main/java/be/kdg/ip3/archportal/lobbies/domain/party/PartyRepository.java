package be.kdg.ip3.archportal.lobbies.domain.party;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface PartyRepository {
    void save(Party party);
}
