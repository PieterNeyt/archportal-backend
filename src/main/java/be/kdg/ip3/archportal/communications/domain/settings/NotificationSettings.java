package be.kdg.ip3.archportal.communications.domain.settings;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.util.Collections;
import java.util.List;


@Entity
@Getter
public class NotificationSettings {
    private final SettingId id;
    private final ProfileId profileId;
    private List<ChannelType> channelType;

    public NotificationSettings(SettingId id, ProfileId profileId, List<ChannelType> channelType) {
        this.id = id;
        this.channelType = channelType;
        this.profileId = profileId;
    }
    public NotificationSettings(ProfileId profileId,List<ChannelType> channelType) {
        this(SettingId.create(), profileId, channelType);
    }

    public List<ChannelType> getChannelType() {
        return Collections.unmodifiableList(channelType);
    }
}
