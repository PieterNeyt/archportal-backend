package be.kdg.ip3.archportal.games.domain.game;

import be.kdg.ip3.archportal.games.domain.Money;
import be.kdg.ip3.archportal.games.domain.NotFoundException;
import be.kdg.ip3.archportal.games.domain.achievement.Achievement;
import be.kdg.ip3.archportal.games.domain.achievement.ExternalAchId;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@AggregateRoot
public class Game {
    private GameId id;
    private GameStudioId studioId;
    private String title;
    private String description;
    private Money price;
    private String imageUrl;
    private String gameUrl;
    private GameGenre genre;
    private int maxLobbySize;

    private final List<Achievement> achievements = new ArrayList<>();

    public Game(GameId id, GameStudioId studioId, String title, String description, BigDecimal price, String imageUrl, String gameUrl, GameGenre genre, int maxLobbySize) {
        this.id = id;
        this.studioId = studioId;
        setTitle(title);
        setDescription(description);
        setPrice(Money.of(price));
        this.imageUrl = imageUrl;
        setGameUrl(gameUrl);
        setGenre(genre);
        setMaxLobbySize(maxLobbySize);
    }

    public Game(GameStudioId studioId, String title, String description, BigDecimal price, String imageUrl, String gameUrl, GameGenre genre, int maxLobbySize) {
        this(GameId.create(), studioId, title, description, price, imageUrl, gameUrl, genre, maxLobbySize);
    }

    public Achievement addAchievement(String title, String description, String imageUrl, GameStudioId id, ExternalAchId externalAchId) {
        checkGameStudioFromOwner(id);
        var achievement = new Achievement(title, description, imageUrl,externalAchId);
        this.achievements.add(achievement);
        return achievement;
    }

    public void addAchievement(Achievement achievement) {
        if (achievement == null) throw new IllegalArgumentException("Achievement cannot be null");
        this.achievements.add(achievement);
    }

    public List<Achievement> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }

    private void setMaxLobbySize(int maxLobbySize) {
        if (maxLobbySize < 0)
            throw new IllegalArgumentException("The provided lobby size is invalid");
        this.maxLobbySize = maxLobbySize;
    }

    private void setGameUrl(String gameUrl) {
        if (gameUrl == null || gameUrl.trim().isEmpty())
            throw new IllegalArgumentException("The provided game url is invalid");
        this.gameUrl = gameUrl;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty() || title.length() > 100)
            throw new IllegalArgumentException("The title provided is invalid.");
        this.title = title;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty() || description.length() > 255)
            throw new IllegalArgumentException("The description provided is invalid.");
        this.description = description;
    }

    public void setPrice(Money price) {
        if (price.money().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("The price provided is invalid.");
        this.price = price;
    }

    public void setGenre(GameGenre genre) {
        if (genre == null)
            throw new IllegalArgumentException("The genre provided is invalid.");
        this.genre = genre;
    }

    public void checkGameStudioFromOwner(GameStudioId id) {
        if(!this.studioId.equals(id))
            throw new IllegalStateException("Wrong GameStudio");
    }

    public void update(Game newGame) {
        checkGameStudioFromOwner(newGame.studioId);
        setTitle(newGame.title);
        setDescription(newGame.description);
        setPrice(newGame.price);
        setImageUrl(newGame.imageUrl);
        setGameUrl(newGame.gameUrl);
        setGenre(newGame.genre);
        setMaxLobbySize(newGame.maxLobbySize);
    }

    private void setImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty() || imageUrl.length() > 255)
            throw new IllegalArgumentException("The imageUrl provided is invalid.");
        this.imageUrl = imageUrl;
    }

    public UUID getAchievementByExternalAchId(String externalAchId) {
        return achievements.stream()
                .filter(a -> a.getExternalAchId().id().equals(externalAchId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Achievement not found"))
                .getId().id();
    }
}