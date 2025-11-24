package be.kdg.ip3.archportal.lobbies.domain;

import be.kdg.ip3.archportal.lobbies.domain.id.GameId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameLobbyId;
import be.kdg.ip3.archportal.lobbies.domain.id.GameSessionId;
import be.kdg.ip3.archportal.lobbies.domain.id.PlayerId;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.time.LocalDateTime;

@Getter
@Entity
public class GameSession {
    private GameSessionId gameSessionId;
    private GameLobbyId gameLobbyId;
    private PlayerId playerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private GameSession(GameSessionId gameSessionId, GameLobbyId gameLobbyId, PlayerId playerId) {
        this.gameSessionId = gameSessionId;
        this.gameLobbyId = gameLobbyId;
        this.playerId = playerId;
        this.startTime = LocalDateTime.now();
        this.endTime = null;
    }

    public GameSession(GameSessionId gameSessionId, GameLobbyId gameLobbyId, PlayerId playerId, LocalDateTime startTime, LocalDateTime endTime) {
        this.gameSessionId = gameSessionId;
        this.gameLobbyId = gameLobbyId;
        this.playerId = playerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static GameSession create(GameLobbyId gameLobbyId, PlayerId playerId) {
        return new GameSession(GameSessionId.create(), gameLobbyId, playerId);
    }

    public void endSession() {
        this.endTime = LocalDateTime.now();
    }


}
