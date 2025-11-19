package be.kdg.ip3.archportal.gameService.application;

import be.kdg.ip3.archportal.gameService.api.dto.GameDto;
import be.kdg.ip3.archportal.gameService.domain.game.Game;
import be.kdg.ip3.archportal.gameService.domain.game.GameId;
import be.kdg.ip3.archportal.gameService.domain.game.GameRepository;
import be.kdg.ip3.archportal.gameService.domain.gamestudio.GameStudioId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final GameStudioService gameStudioService;

    public GameService(GameRepository gameRepository, GameStudioService gameStudioService) {
        this.gameRepository = gameRepository;
        this.gameStudioService = gameStudioService;
    }

    public Game createGame(GameDto gameDto) {
        var studio = gameStudioService.findById(new GameStudioId(gameDto.studioId()));
        var game = new Game(GameId.create(), studio.getId(), gameDto.title(), gameDto.description(),
                gameDto.price(), gameDto.imageUrl(), gameDto.genre());
        gameRepository.save(game);
        return game;
    }
}
