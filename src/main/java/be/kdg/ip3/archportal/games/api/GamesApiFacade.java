package be.kdg.ip3.archportal.games.api;

import be.kdg.ip3.archportal.games.GamesApi;
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
}
