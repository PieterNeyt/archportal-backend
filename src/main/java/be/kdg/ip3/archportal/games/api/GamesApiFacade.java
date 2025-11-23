package be.kdg.ip3.archportal.games.api;

import be.kdg.ip3.archportal.games.shared.GameDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.application.GameService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GamesApiFacade implements GamesApi {

    private final GameService gameService;

    public GamesApiFacade(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public List<UUID> validateGames(List<UUID> gameIds) {
        return gameService.validateGames(gameIds);
    }

    @Override
    public List<GameDto> getAllGames() {
        var games = gameService.findAll();
        return games.stream().map(GameDto::fromDomain).toList();
    }

    @Override
    public List<GameDto> getGamesByIds(List<UUID> gameIds) {
        var games = gameService.findByIds(gameIds);
        return games.stream().map(GameDto::fromDomain).toList();
    }
}
