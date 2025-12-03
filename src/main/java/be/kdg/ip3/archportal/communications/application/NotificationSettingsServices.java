package be.kdg.ip3.archportal.communications.application;


import be.kdg.ip3.archportal.communications.api.dto.ChangeChannelTypeDto;
import be.kdg.ip3.archportal.communications.api.dto.NotificationSettingsDto;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import be.kdg.ip3.archportal.communications.domain.settings.SettingId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationSettingsServices {
    private final NotificationSettingsRepository repository;

    public NotificationSettingsServices(NotificationSettingsRepository repository) {
        this.repository = repository;
    }

    public NotificationSettingsDto addChannelType(ChangeChannelTypeDto addChannelTypeDto, ProfileId profileId) {
        var settings = repository.findById(new SettingId(addChannelTypeDto.settingId()))
                .orElse(new NotificationSettings(profileId));

        settings.addChannelType(addChannelTypeDto.channelType());

        this.repository.save(settings);

        return new NotificationSettingsDto(settings.getId().id(),settings.getChannelType());
    }

    public NotificationSettingsDto removeChannelType(ChangeChannelTypeDto removeChannelTypeDto, ProfileId profileId) {
        var settings = repository.findById(new SettingId(removeChannelTypeDto.settingId()))
                .orElse(new NotificationSettings(profileId));

        settings.removeChannelType(removeChannelTypeDto.channelType());

        this.repository.save(settings);

        return new NotificationSettingsDto(settings.getId().id(),settings.getChannelType());
    }
}

