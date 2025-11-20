package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.games.api.dto.GameDto;
import be.kdg.ip3.archportal.games.domain.game.Game;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.domain.gamestudio.GameStudioId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        var game = new Game(studio.getId(), gameDto.title(), gameDto.description(),
                gameDto.price(), gameDto.imageUrl(), gameDto.gameUrl(), gameDto.genre());
        gameRepository.save(game);
        return game;
    }

    public List<UUID> validateGames(List<UUID> gameIds) {
        return gameIds.stream()
                .filter(gameId -> !gameRepository.existsById(gameId))
                .toList();
    }


    public List<Game> findAll(){
        return gameRepository.findAll();
    }
}
