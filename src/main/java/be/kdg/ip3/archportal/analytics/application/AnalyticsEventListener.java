package be.kdg.ip3.archportal.analytics.application;

import be.kdg.ip3.archportal.analytics.shared.CreateGameStatsDto;
import be.kdg.ip3.archportal.lobbies.shared.SessionEndedEvent;
import be.kdg.ip3.archportal.profiles.shared.GameAddedToLibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AnalyticsEventListener {

    private final AnalyticsService analyticsService;

    public AnalyticsEventListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Async
    @EventListener
    public void handleGameAddedToLibrary(GameAddedToLibraryEvent event) {
        List<CreateGameStatsDto> createGameStatsDtos = new ArrayList<>();
        createGameStatsDtos.add(new CreateGameStatsDto(event.getGameId(), event.getProfileId()));

        analyticsService.instantiateGameStatistics(createGameStatsDtos);
    }

    @Async
    @EventListener
    public void handleSessionEnd(SessionEndedEvent event) {
        var command = GameStatsCommand.fromEvent(event);

        analyticsService.updateStatistics(command);
    }
}