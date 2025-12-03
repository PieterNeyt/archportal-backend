package be.kdg.ip3.archportal.communications.infrastructure.notificationsettings.jpa;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "notification_settings", schema ="communicationservice")
public class JpaNotificationSettingsEntity {
    @Id
    @Column(nullable = false)
    private UUID profileId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "notification_channel_types",
            schema = "communicationservice",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "channel_type")
    private List<ChannelType> channelTypes;

    protected JpaNotificationSettingsEntity() {
    }

    private JpaNotificationSettingsEntity(UUID profileId, List<ChannelType> channelTypes) {
        this.profileId = profileId;
        this.channelTypes = channelTypes;
    }

    public static JpaNotificationSettingsEntity fromDomain(NotificationSettings settings) {
        return new JpaNotificationSettingsEntity(
                settings.getProfileId().id(),
                settings.getChannelType()
        );
    }

    public NotificationSettings toDomain() {
        return new NotificationSettings(
                new ProfileId(profileId),
                this.channelTypes
        );
    }
}