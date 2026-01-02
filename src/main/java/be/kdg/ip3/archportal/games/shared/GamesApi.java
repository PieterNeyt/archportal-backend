package be.kdg.ip3.archportal.games.shared;

import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NamedInterface
public interface GamesApi {
    List<UUID> validateGames(List<UUID> gameIds);
    String getGameUrl(UUID gameId);

    List<GlobalGameDto> getAllGames();
    List<GlobalGameDto> getGamesByIds(List<UUID> gameIds);

    List<AchievementDto> getAchievements(Map<UUID, LocalDateTime> achievementData, UUID gameId);

    GlobalGameDto getGameById(UUID gameId);
    int getMaxPlayersForGame(UUID gameId);

    boolean validateGame(UUID uuid);

    GrantedAchievementDto findAchievementByExternalAchId(String externalAchId, UUID gameId);

    List<AchievementDto> getAllAchievements(Map<UUID, LocalDateTime> achievementIds,List<UUID> gameIds);
}
