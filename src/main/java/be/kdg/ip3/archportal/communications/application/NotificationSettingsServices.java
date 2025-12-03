package be.kdg.ip3.archportal.communications.application;


import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationSettingsServices {
    private final NotificationSettingsRepository repository;

    public NotificationSettingsServices(NotificationSettingsRepository repository) {
        this.repository = repository;
    }

    public List<ChannelType> addChannelType(ChannelType addChannelType, ProfileId profileId) {
        var settings = repository.findById(profileId)
                .orElse(new NotificationSettings(profileId));

        settings.addChannelType(addChannelType);

        this.repository.save(settings);

        return settings.getChannelType();
    }

    public List<ChannelType> removeChannelType(ChannelType removeChannelType, ProfileId profileId) {
        var settings = repository.findById(profileId)
                .orElse(new NotificationSettings(profileId));

        settings.removeChannelType(removeChannelType);

        this.repository.save(settings);

        return settings.getChannelType();
    }

    public void createNotificationSettings(UUID profileId) {
        var settings = new NotificationSettings(new ProfileId(profileId));
        this.repository.save(settings);
    }
}

