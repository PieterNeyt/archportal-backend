package be.kdg.ip3.archportal.games;

import java.util.List;
import java.util.UUID;

public interface GamesApi {
    // returnt een lijst van invalide game id's indien die er zijn
    List<UUID> validateGames(List<UUID> gameIds);
}
