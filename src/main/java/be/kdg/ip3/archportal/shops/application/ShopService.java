package be.kdg.ip3.archportal.shops.application;

import be.kdg.ip3.archportal.games.shared.GameDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ShopService {
    private final GamesApi gameApi;

    public ShopService(GamesApi gameApi) {
        this.gameApi = gameApi;
    }

    public List<GameDto> getAllGames() {
        return gameApi.getAllGames();
    }
}
