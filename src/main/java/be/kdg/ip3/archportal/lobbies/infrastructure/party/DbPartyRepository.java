package be.kdg.ip3.archportal.lobbies.infrastructure.party;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import be.kdg.ip3.archportal.lobbies.domain.party.Party;
import be.kdg.ip3.archportal.lobbies.domain.party.PartyRepository;
import be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa.JpaPartyEntity;
import be.kdg.ip3.archportal.lobbies.infrastructure.party.jpa.JpaPartyRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    @Override
    public boolean existsByPlayerId(PlayerId playerId) {
        return jpaPartyRepository.existsByPlayerId(playerId.id());
    }

    @Override
    public Optional<Party> findByMemberId(PlayerId playerId) {
        return this.jpaPartyRepository.findByMemberId(playerId.id()).map(JpaPartyEntity::toDomain);
    }
}
