package be.kdg.ip3.archportal.communications.domain.settings;

import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.util.Collections;
import java.util.List;


@Entity
@Getter
public class NotificationSettings {
    private final ProfileId profileId;
    private List<ChannelType> channelType;

    public NotificationSettings(ProfileId profileId, List<ChannelType> channelType) {
        this.channelType = channelType;
        this.profileId = profileId;
    }

    public NotificationSettings(ProfileId profileId) {
        this(profileId, List.of(ChannelType.EMAIL, ChannelType.IN_PLATFORM));
    }

    public void addChannelType(ChannelType channelType) {
        if (this.channelType.contains(channelType)) {
            throw new IllegalArgumentException("Cant add the same channel type to preferences twice!");
        }
        IO.println(channelType);
        this.channelType.add(channelType);
    }

    public void removeChannelType(ChannelType channelType) {
        if (!this.channelType.contains(channelType)) {
            throw new IllegalArgumentException("Cant remove a channel type that is not in your preferences!");
        }
        IO.println(channelType);
        this.channelType.remove(channelType);
    }
}
