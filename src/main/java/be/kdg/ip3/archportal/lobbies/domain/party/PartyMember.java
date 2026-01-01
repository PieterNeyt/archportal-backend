package be.kdg.ip3.archportal.lobbies.domain.party;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

@Getter
@Entity
public class PartyMember {
    private final PlayerId playerId;
    private boolean host;
    private boolean ready;

    public PartyMember(PlayerId playerId, boolean host, boolean ready) {
        this.playerId = playerId;
        this.host = host;
        this.ready = ready;
    }
    public PartyMember(PlayerId playerId) {
        this.playerId = playerId;
        this.host = false;
        this.ready = false;
    }

    public void toggleReady() {
        this.ready = !this.ready;
    }

    public void makeHost() {
        this.host = true;
    }
}
