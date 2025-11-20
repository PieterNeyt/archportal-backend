package be.kdg.ip3.archportal.profiles.domain.library;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Library {
    private final LibraryId id;
    private List<UUID> games;

    public Library(LibraryId id, List<UUID> games) {
        this.id = id;
        this.games = games;

    }

    public Library(List<UUID> games) {
        this.id = LibraryId.create();
        this.games =games;
    }

    public void AquireGame(UUID gameId){
        this.games.add(gameId);
    }
}
