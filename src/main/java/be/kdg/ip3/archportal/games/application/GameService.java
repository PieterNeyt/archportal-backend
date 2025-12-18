package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.games.application.command.AchievementCommand;
import be.kdg.ip3.archportal.games.application.command.GameCommand;
import be.kdg.ip3.archportal.games.domain.NotFoundException;
import be.kdg.ip3.archportal.games.domain.achievement.Achievement;
import be.kdg.ip3.archportal.games.domain.achievement.ExternalAchId;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameId;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final GameStudioService gameStudioService;
    private final ApplicationEventPublisher eventPublisher;

    public GameService(GameRepository gameRepository, GameStudioService gameStudioService, ApplicationEventPublisher eventPublisher) {
        this.gameRepository = gameRepository;
        this.gameStudioService = gameStudioService;
        this.eventPublisher = eventPublisher;
    }

    public Game createGame(GameCommand gameCommand, OwnerId ownerId) {
        var studio = gameStudioService.findByOwnerId(ownerId);
        studio.checkOwner(ownerId);

        var game = gameCommand.toDomain(studio.getId());

        gameRepository.save(game);

        eventPublisher.publishEvent(new AddNotificationEvent(ownerId.id(),
                String.format("Congrats! You have successfully created your own game %s", game.getTitle()),
                "Now that your project is live, you can head over to the Game Studio page to continue building your experience.\n" +
                        "From there, you can add new features, update existing content, customize your game world, or even create your own achievements and updates to share with your players.\n" +
                        "\n" +
                        "Feel free to explore, experiment, and shape your game exactly the way you imagine it.\n" +
                        "\n" +
                        "Kind regards,\n" +
                        "The Arch Portal Team",
                NotificationType.SYSTEM
        ));
        return game;
    }

    public List<Game> getGamesFromStudio(OwnerId ownerId) {
        var studio = gameStudioService.findByOwnerId(ownerId);
        return this.gameRepository.findByStudioId(studio.getId());
    }

    public Game updateGame(GameCommand gameCommand, OwnerId ownerId) {
        var game = this.gameRepository.findById(gameCommand.id())
                .orElseThrow(() -> new NotFoundException("game not found"));

        var gameStudio = this.gameStudioService.findByOwnerId(ownerId);

        game.update(gameCommand.toDomain(gameStudio.getId()));

        this.gameRepository.save(game);
        return game;
    }

    public Achievement addAchievement(AchievementCommand achievementCommand, OwnerId ownerId, GameId gameId) {
        var game = this.gameRepository.findById(gameId.id())
                .orElseThrow(() -> new NotFoundException("game not found"));

        var gameStudio = this.gameStudioService.findByOwnerId(ownerId);

        var achievement = game.addAchievement(
                achievementCommand.title(), achievementCommand.description(),
                achievementCommand.imageUrl(),gameStudio.getId(),new ExternalAchId(achievementCommand.externalAchId()));

        this.gameRepository.save(game);
        return achievement;
    }
}
