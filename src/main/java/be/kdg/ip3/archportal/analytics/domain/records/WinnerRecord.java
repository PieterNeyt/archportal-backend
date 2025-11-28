package be.kdg.ip3.archportal.analytics.domain.records;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

public record WinnerRecord(
        LocalDateTime PlayedAt,
        String Winner,            // "X" / "O" / "DRAW"
        SessionId SessionId      // Optional
){

}
