package be.kdg.ip3.archportal.profiles.shared;// be.kdg.ip3.archportal.profiles.application.events.GameAddedToLibraryEvent.java (maak dit bestand aan)

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class GameAddedToLibraryEvent extends ApplicationEvent {
    private final UUID gameId;
    private final UUID profileId;

    public GameAddedToLibraryEvent(Object source, UUID gameId, UUID profileId) {
        super(source);
        this.gameId = gameId;
        this.profileId = profileId;
    }

}