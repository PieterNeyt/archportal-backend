package be.kdg.ip3.archportal.games.infrastructure.game.jpa;

import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "games", schema = "gameservice")
public class JpaGameEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID studioId;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    private String imageUrl;
    @Column(nullable = false)
    private String gameUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameGenre genre;
    // TODO connection to achievement and updates

    protected JpaGameEntity() {
    }

    public JpaGameEntity(UUID id, UUID studioId, String title, String description, BigDecimal price, String imageUrl, String gameUrl, GameGenre genre) {
        this.id = id;
        this.studioId = studioId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.gameUrl = gameUrl;
        this.genre = genre;
    }

    public static JpaGameEntity fromDomain(Game game) {
        return new JpaGameEntity(game.getId().id(), game.getStudioId().id(), game.getTitle(), game.getDescription(),
                game.getPrice().money(), game.getImageUrl(), game.getGameUrl(), game.getGenre());
    }
}
