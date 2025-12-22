package be.kdg.ip3.archportal.lobbies.domain.partyInvite;

import be.kdg.ip3.archportal.lobbies.domain.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
public class PartyInvite {
    private PartyInviteId id;
    private PlayerId senderId;
    private PlayerId receiverId;

    public PartyInvite(PartyInviteId id, PlayerId senderId, PlayerId receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public PartyInvite(PlayerId senderId, PlayerId receiverId) {
        this.id = new PartyInviteId(UUID.randomUUID());
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PartyInvite that = (PartyInvite) o;
        return Objects.equals(receiverId, that.receiverId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(receiverId);
    }
}
