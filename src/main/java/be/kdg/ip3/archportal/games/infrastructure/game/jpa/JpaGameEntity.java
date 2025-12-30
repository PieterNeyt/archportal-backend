package be.kdg.ip3.archportal.games.infrastructure.game.jpa;

import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameGenre;
import be.kdg.ip3.archportal.games.domain.game.GameId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "games", schema = "gameservice")
public class JpaGameEntity {
    @Id
    private UUID id;
    @Column
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
    @Column(nullable = false)
    private int maxLobbySize;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<JpaAchievementEntity> achievements = new ArrayList<>();

    protected JpaGameEntity() {
    }

    public JpaGameEntity(UUID id, UUID studioId, String title, String description, BigDecimal price, String imageUrl, String gameUrl, GameGenre genre, int maxLobbySize) {
        this.id = id;
        this.studioId = studioId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.gameUrl = gameUrl;
        this.genre = genre;
        this.maxLobbySize = maxLobbySize;
    }

    public static JpaGameEntity fromDomain(Game game) {
        JpaGameEntity entity = new JpaGameEntity(
                game.getId().id(),
                game.getStudioId().id(),
                game.getTitle(),
                game.getDescription(),
                game.getPrice().money(),
                game.getImageUrl(),
                game.getGameUrl(),
                game.getGenre(),
                game.getMaxLobbySize()
        );

        if (game.getAchievements() != null) {
            entity.achievements = game.getAchievements().stream()
                    .map(a -> JpaAchievementEntity.fromDomain(a, entity))
                    .collect(Collectors.toList());
        }

        return entity;
    }

    public Game toDomain() {
        Game game = new Game(
                new GameId(id),
                new GameStudioId(studioId),
                title,
                description,
                price,
                imageUrl,
                gameUrl,
                genre,
                maxLobbySize
        );

        if (achievements != null) {
            achievements.forEach(a -> game.addAchievement(a.toDomain()));
        }

        return game;
    }
}