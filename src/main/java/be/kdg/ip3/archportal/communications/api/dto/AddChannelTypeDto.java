package be.kdg.ip3.archportal.communications.api.dto;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;

import java.util.UUID;

public record AddChannelTypeDto(UUID settingId, ChannelType channelType) {
}
