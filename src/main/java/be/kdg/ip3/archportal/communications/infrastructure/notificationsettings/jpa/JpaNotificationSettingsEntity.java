package be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.jpa;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import be.kdg.ip3.archportal.communications.domain.settings.SettingId;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "notification_settings", schema ="communicationservice")
public class JpaNotificationSettingsEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID profileId;

    @ElementCollection(targetClass = ChannelType.class)
    @CollectionTable(name = "notification_channel_types", joinColumns = @JoinColumn(name = "notification_settings_id"))
    @Column(name = "channel_type")
    @Enumerated(EnumType.STRING)
    private List<ChannelType> channelTypes;

    protected JpaNotificationSettingsEntity() {
    }

    private JpaNotificationSettingsEntity(UUID id, UUID profileId, List<ChannelType> channelTypes) {
        this.id = id;
        this.profileId = profileId;
        this.channelTypes = channelTypes;
    }

    public static JpaNotificationSettingsEntity fromDomain(NotificationSettings settings) {
        return new JpaNotificationSettingsEntity(
                settings.getId().id(),
                settings.getProfileId().id(),
                settings.getChannelType()
        );
    }

    public NotificationSettings toDomain() {
        return new NotificationSettings(
                new SettingId(id),
                new ProfileId(profileId),
                this.channelTypes
        );
    }
}