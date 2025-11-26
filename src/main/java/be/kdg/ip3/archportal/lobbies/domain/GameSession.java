package be.kdg.ip3.archportal.lobbies.domain;

import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.time.LocalDateTime;

@Getter
@Entity
public class GameSession {
    @Identity
    private GameSessionId gameSessionId;
    private GameLobbyId gameLobbyId;
    private PlayerId playerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String launchUrl;

    private GameSession(GameSessionId gameSessionId, GameLobbyId gameLorbbyId, PlayerId playerId, String launchUrl) {
        this.gameSessionId = gameSessionId;
        this.playerId = playerId;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
        this.launchUrl = launchUrl;
    }

    public GameSession(GameSessionId gameSessionId, GameLobbyId gameLobbyId, PlayerId playerId, LocalDateTime startTime, LocalDateTime endTime, String launchUrl) {
        this.gameSessionId = gameSessionId;
        this.gameLobbyId = gameLobbyId;
        this.playerId = playerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.launchUrl = launchUrl;
    }

    public static GameSession create(GameLobbyId gameLobbyId,
                                     PlayerId playerId,
                                     String baseLaunchUrl) {

        var sessionId = GameSessionId.create();
        String launchUrl = baseLaunchUrl + "session/" + sessionId.id();

        return new GameSession(sessionId, gameLobbyId, playerId, launchUrl);
    }

    public void endSession() {
        this.endTime = LocalDateTime.now();
    }


}
