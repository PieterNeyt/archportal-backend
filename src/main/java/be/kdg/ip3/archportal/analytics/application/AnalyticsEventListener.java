package be.kdg.ip3.archportal.analytics.application;// be.kdg.ip3.archportal.analytics.application.AnalyticsEventListener.java (maak dit bestand aan)

import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnalyticsEventListener {

    private final AnalyticsService analyticsService;

    public AnalyticsEventListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @EventListener
    public void handleGameAddedToLibrary(be.kdg.ip3.archportal.profiles.shared.GameAddedToLibraryEvent event) {
        List<CreateGameStatsDto> createGameStatsDtos = new ArrayList<>();
        createGameStatsDtos.add(new CreateGameStatsDto(event.getGameId(), event.getProfileId()));

        analyticsService.instantiateGameStatistics(createGameStatsDtos);
    }
}