package be.kdg.ip3.archportal.games.shared;

import org.springframework.modulith.NamedInterface;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NamedInterface
public interface GamesApi {
    // returnt een lijst van invalide game id's indien die er zijn
    List<UUID> validateGames(List<UUID> gameIds);
    String getGameUrl(UUID gameId);

    List<GlobalGameDto> getAllGames();
    List<GlobalGameDto> getGamesByIds(List<UUID> gameIds);

    GlobalGameDto getGameById(UUID gameId);
    int getMaxPlayersForGame(UUID gameId);

    boolean validateGame(UUID uuid);
}
