package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository {
    void save(Party party);

    Optional<Party> findByMemberId(PlayerId playerId);

    boolean existsByPlayerId(PlayerId playerId);

    List<Party> findPartyHasInvite(PlayerId playerId);

    Optional<Party> findById(PartyId id);

    void deleteById(PartyId partyId);
}
