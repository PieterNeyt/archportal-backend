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
    private final List<ChannelType> channelType;

    public NotificationSettings(ProfileId profileId, List<ChannelType> channelType) {
        this.channelType = channelType;
        this.profileId = profileId;
    }

    public NotificationSettings(ProfileId profileId) {
        this(profileId, List.of(ChannelType.EMAIL, ChannelType.IN_PLATFORM));
    }

    public List<ChannelType> getChannelType() {
        return Collections.unmodifiableList(channelType);
    }

    public void addChannelType(ChannelType channelType) {
        var alreadyExist = this.channelType.stream().
                anyMatch(c -> c.equals(channelType));

        if (alreadyExist) {
            throw new IllegalArgumentException("Cant add the same channel type to preferences twice!");
        }

        this.channelType.add(channelType);
    }

    public void removeChannelType(ChannelType channelType) {
        var alreadyExist = this.channelType.stream().
                noneMatch(c -> c.equals(channelType));

        if (alreadyExist) {
            throw new IllegalArgumentException("Cant remove a channel type that is not in youre preferences!");
        }

        this.channelType.remove(channelType);
    }
}
