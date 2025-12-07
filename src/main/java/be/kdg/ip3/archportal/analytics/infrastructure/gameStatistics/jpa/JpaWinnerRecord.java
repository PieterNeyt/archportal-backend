package be.kdg.ip3.archportal.analytics.infrastructure.gameStatistics.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@Getter
public class JpaWinnerRecord {

    @Column(nullable = false)
    private LocalDateTime playedAt;
    @Column(nullable = false)
    private String winner;
    @Column(nullable = false)
    private UUID sessionId;

    protected JpaWinnerRecord() { }

    public JpaWinnerRecord(LocalDateTime playedAt, String winner, UUID sessionId) {
        this.playedAt = playedAt;
        this.winner = winner;
        this.sessionId = sessionId;
    }
}
