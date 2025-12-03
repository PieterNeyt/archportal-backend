package be.kdg.ip3.archportal.communications.api.dto;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;

import java.util.List;

public record NotificationSettingsDto(List<ChannelType> channels) {
}
