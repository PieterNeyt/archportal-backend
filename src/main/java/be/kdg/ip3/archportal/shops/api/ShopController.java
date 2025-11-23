package be.kdg.ip3.archportal.shops.api;

import be.kdg.ip3.archportal.games.shared.GameDto;
import be.kdg.ip3.archportal.shops.application.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/games")
    public ResponseEntity<List<GameDto>> getAllGames() {
        var games = shopService.getAllGames();
        return ResponseEntity.ok(games);
    }

}
