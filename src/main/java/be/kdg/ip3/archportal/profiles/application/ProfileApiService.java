package be.kdg.ip3.archportal.profiles.application;

import be.kdg.ip3.archportal.analytics.shared.AnalyticsApi;
import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import be.kdg.ip3.archportal.games.shared.GamesApi;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileId;
import be.kdg.ip3.archportal.profiles.domain.profile.ProfileRepository;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProfileApiService implements ProfilesApi {
    private final ProfileRepository profileRepository;
    private final GamesApi gamesApi;
    private final AnalyticsApi analyticsApi;

    public ProfileApiService(ProfileRepository profileRepository, GamesApi gamesApi, AnalyticsApi analyticsApi) {
        this.profileRepository = profileRepository;
        this.gamesApi = gamesApi;
        this.analyticsApi = analyticsApi;
    }

    @Override
    public boolean existsById(UUID id) {
        var profileId = new ProfileId(id);
        return profileRepository.existsById(profileId);
    }

    @Override
    public String getProfileEmail(UUID id) {
        var profileId = new ProfileId(id);
        var profile = profileRepository.findById(profileId)
                .orElseThrow(profileId::notFound);

        return profile.getEmail();
    }

    @Override
    public void checkAlreadyOwnsGames(UUID profileId, List<UUID> games) {
        var profile = profileRepository.findById(new ProfileId(profileId)).orElseThrow(new ProfileId(profileId)::notFound);

        List<UUID> alreadyOwned = games.stream()
                .filter(profile::hasGame)
                .toList();

        if (!alreadyOwned.isEmpty()) {
            throw new IllegalArgumentException(
                    "Profile %s already owns the following games: %s"
                            .formatted(profileId, alreadyOwned)
            );
        }
    }

    @Override
    public void checkAlreadyOwnsGame(UUID profileId, UUID gameId) {
        var profile = profileRepository.findById(new ProfileId(profileId)).orElseThrow(new ProfileId(profileId)::notFound);
        profile.hasGameCheck(gameId);
    }

    @Override
    public void addGamesToLibrary(UUID profileId, List<UUID> games) {
        List<UUID> nonExistentGames = gamesApi.validateGames(games);
        if (!nonExistentGames.isEmpty()) {
            throw new IllegalArgumentException("The following games do not exist: " + nonExistentGames);
        }

        checkAlreadyOwnsGames(profileId, games);
        var id = new ProfileId(profileId);
        var profile = profileRepository.findById(id).orElseThrow(id::notFound);

        games.forEach(profile::acquireGame);
        profileRepository.save(profile);

        for (var game: games){
            List<CreateGameStatsDto> createGameStatsDtos = new ArrayList<>();
            createGameStatsDtos.add(new CreateGameStatsDto(game, profileId));

            analyticsApi.instantiateGameStatistics(createGameStatsDtos);
        }

    }
}
