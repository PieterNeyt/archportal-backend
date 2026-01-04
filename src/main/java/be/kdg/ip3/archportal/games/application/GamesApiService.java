package be.kdg.ip3.archportal.games.application;

import be.kdg.ip3.archportal.games.domain.NotFoundException;
import be.kdg.ip3.archportal.games.domain.game.GameId;
import be.kdg.ip3.archportal.games.domain.game.GameRepository;
import be.kdg.ip3.archportal.games.shared.AchievementDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.games.shared.GlobalGameDto;
import be.kdg.ip3.archportal.games.shared.GrantedAchievementDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class GamesApiService implements GamesApi {
    private final GameRepository gameRepository;

    public GamesApiService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<GlobalGameDto> getAllGames() {
        var games = gameRepository.findAll();
        return games.stream().map(GlobalGameDto::fromDomain).toList();
    }

    @Override
    public List<GlobalGameDto> getGamesByIds(List<UUID> gameIds) {
        var games = gameRepository.findAllById(gameIds);
        return games.stream().map(GlobalGameDto::fromDomain).toList();
    }

    @Override
    public List<AchievementDto> getAchievements(Map<UUID, LocalDateTime> achievementData, UUID gameId) {
        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("Game not found"));

        return game.getAchievements().stream()
                .filter(achievement -> achievementData.containsKey(achievement.getId().id()))
                .map(achievement -> {
                    LocalDateTime unlockedAt = achievementData.get(achievement.getId().id());
                    return AchievementDto.fromDomain(achievement, unlockedAt);
                })
                .toList();
    }

    @Override
    public List<AchievementDto> getAllAchievements(Map<UUID, LocalDateTime> achievementIds, List<UUID> gameIds) {
        var games = gameRepository.findAllById(gameIds);
        return games.stream()
                .flatMap(game -> game.getAchievements().stream())
                .filter(achievement -> achievementIds.containsKey(achievement.getId().id()))
                .map(achievement -> {
                    LocalDateTime unlockedAt = achievementIds.get(achievement.getId().id());
                    return AchievementDto.fromDomain(achievement, unlockedAt);
                })
                .toList();
    }

    @Override
    public UUID getGameIdByName(String gameName) {
        return gameRepository.findIdByName(gameName)
                .orElseThrow(() -> new NotFoundException("Game with name [" + gameName + "] not found"));
    }


    @Override
    public GlobalGameDto getGameById(UUID gameId) {
        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("Game Id[" + gameId + "] not found"));

        return GlobalGameDto.fromDomain(game);
    }

    @Override
    public int getMaxPlayersForGame(UUID gameId) {
        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("Game Id[" + gameId + "] not found"));

        return game.getMaxLobbySize();
    }

    @Override
    public boolean validateGame(UUID uuid) {
        return gameRepository.existsById(uuid);
    }

    @Override
    public GrantedAchievementDto findAchievementByExternalAchId(String externalAchId, UUID gameId) {
        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("Game not found"));

        var achievement = game.getAchievementByExternalAchId(externalAchId);
        return new GrantedAchievementDto(achievement.getId().id(), achievement.getTitle());
    }

    @Override
    public List<UUID> validateGames(List<UUID> gameIds) {
        return gameIds.stream()
                .filter(gameId -> !gameRepository.existsById(gameId))
                .toList();
    }

    @Override
    public String getGameUrl(UUID gameId) {
        var game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game with id " + gameId + " not found"));
        return game.getGameUrl();
    }
}
