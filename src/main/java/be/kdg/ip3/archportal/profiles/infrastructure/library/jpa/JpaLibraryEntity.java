package be.kdg.ip3.archportal.profiles.infrastructure.library.jpa;

import be.kdg.ip3.archportal.profiles.domain.library.Library;
import be.kdg.ip3.archportal.profiles.domain.library.LibraryId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "library", schema = "profileservice")
public class JpaLibraryEntity {

    @Id
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "library_games", joinColumns = @JoinColumn(name = "library_id"))
    @Column(name = "game_id", nullable = false)
    private List<UUID> games = new ArrayList<>();

    public JpaLibraryEntity() {}

    public JpaLibraryEntity(UUID id, List<UUID> games) {
        this.id = id;
        this.games = games;
    }

    public JpaLibraryEntity(List<UUID> games) {
        this.id = LibraryId.create().id();
        this.games = games;
    }

    public static JpaLibraryEntity fromDomain(Library library) {
        return new JpaLibraryEntity(
                library.getId().id(),
                library.getGames()
        );
    }

    public Library toDomain() {
        return new Library(
                new LibraryId(id),
                games
        );
    }
}