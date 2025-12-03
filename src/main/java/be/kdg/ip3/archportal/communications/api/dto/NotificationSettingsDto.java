package be.kdg.ip3.archportal.communications.api.dto;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;

import java.util.List;
import java.util.UUID;

public record NotificationSettingsDto(UUID SettingId, List<ChannelType> channelType){
    public NotificationSettingsDto fromDomain(NotificationSettings settings){
        return new NotificationSettingsDto(settings.getId().id(),settings.getChannelType());
    }
}
