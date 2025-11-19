package be.kdg.ip3.archportal.gameService.domain.game;

import be.kdg.ip3.archportal.gameService.domain.Money;
import be.kdg.ip3.archportal.gameService.domain.achievement.Achievement;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.gameService.domain.update.Update;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AggregateRoot
public class Game {
    private GameId id;
    private GameStudioId studioId;
    private String title;
    private String description;
    private Money price;
    private String imageUrl;
    private GameGenre genre;
    private List<Achievement> achievements;
    private List<Update> updates;

    public Game(GameId id, GameStudioId studioId, String title, String description, BigDecimal price, String imageUrl, GameGenre genre) {
        this.id = id;
        this.studioId = studioId;
        setTitle(title);
        setDescription(description);
        setPrice(Money.of(price));
        this.imageUrl = imageUrl;
        setGenre(genre);
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty() || title.isEmpty() || title.length() > 100)
            throw new IllegalArgumentException("The title provided is invalid.");
        this.title = title;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty() || description.isEmpty() || description.length() > 255)
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
}
