package be.kdg.ip3.archportal.analytics.api.dto;

import be.kdg.ip3.archportal.analytics.domain.records.SessionId;
import lombok.Getter;

import java.time.LocalDateTime;

public record WinnerRecordDto(
        LocalDateTime PlayedAt,
        String Winner,
        SessionId SessionId
) {
}
