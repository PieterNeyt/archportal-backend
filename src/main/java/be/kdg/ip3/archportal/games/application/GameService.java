package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.games.api.dto.GameDto;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import be.kdg.ip3.archportal.games.domain.owner.OwnerId;
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

    public Game createGame(GameDto gameDto, OwnerId ownerId) {
        var studio = gameStudioService.findById(new GameStudioId(gameDto.studioId()));
        studio.checkOwner(ownerId);
        
        var game = new Game(studio.getId(), gameDto.title(), gameDto.description(),
                gameDto.price(), gameDto.imageUrl(), gameDto.gameUrl(), gameDto.genre());
        gameRepository.save(game);
        return game;
    }
}
