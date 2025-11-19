package be.kdg.ip3.archportal.gameService.domain.game;

import be.kdg.ip3.archportal.gameService.domain.Money;
import be.kdg.ip3.archportal.gameService.domain.achievement.AchievementId;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.gameService.domain.update.UpdateId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;

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
    private List<AchievementId> achievements;
    private List<UpdateId> updates;
}
