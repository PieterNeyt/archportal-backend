package be.kdg.ip3.archportal.communications.notifications;

import be.kdg.ip3.archportal.communications.application.NotificationSettingsServices;
import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import be.kdg.ip3.archportal.communications.domain.notification.ChannelType;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveAddChannelTypesNotificationSettingsSociableTest {

    @Mock
    NotificationSettingsRepository notificationSettingsRepository;

    NotificationSettingsServices service;

    @BeforeEach
    void setUp() {
        service = new NotificationSettingsServices(notificationSettingsRepository);
    }

    @Nested
    class AddChannelType {
        @Test
        void addChannelType_existingProfile_addsChannelTypeAndSaves() {
            var profileId = new ProfileId(UUID.randomUUID());
            var settings = new NotificationSettings(profileId);


            var channelTypeToAdd = ChannelType.values()[0];

            //zorgen dat de gekozen al nog niet in de lijst staat
            if(settings.getChannelType().contains(channelTypeToAdd)) {
                settings.removeChannelType(channelTypeToAdd);
            }

            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.of(settings));

            // Act
            service.addChannelType(channelTypeToAdd, profileId);

            // Assert
            assertThat(settings.getChannelType()).contains(channelTypeToAdd);
            verify(notificationSettingsRepository).save(settings);
        }

        @Test
        void addChannelType_profileNotFound_throwsNotFoundException() {
            var profileId = new ProfileId(UUID.randomUUID());
            var channelToAdd = ChannelType.values()[0];

            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.addChannelType(channelToAdd,profileId ))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Profile [" + profileId.id() +"] not found");
        }

        @Test
        void addChannelType_duplicateChannel_throwsIllegalArgumentException() {
            var profileId = new ProfileId(UUID.randomUUID());
            var settings = new NotificationSettings(profileId);

            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.of(settings));

            // Act & Assert
            assertThatThrownBy(() -> service.addChannelType(settings.getChannelType().getFirst(), profileId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cant add the same channel type to preferences twice!");
        }
    }

    @Nested
    class RemoveChannelType {
        @Test
        void removeChannelType_existingChannel_removesAndSaves() {
            var profileId = new ProfileId(UUID.randomUUID());
            var settings = new NotificationSettings(profileId);

            if (settings.getChannelType().isEmpty())
                settings.addChannelType(ChannelType.values()[0]);

            var channelToRemove = settings.getChannelType().getFirst();

            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.of(settings));

            // Act
            var result = service.removeChannelType(channelToRemove, profileId);

            // Assert
            assertThat(result.channels()).doesNotContain(channelToRemove);
            verify(notificationSettingsRepository).save(settings);
        }

        @Test
        void removeChannelType_profileNotFound_throwsNotFoundException() {
            var profileId = new ProfileId(UUID.randomUUID());
            var channelToRemove = ChannelType.values()[0];

            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.removeChannelType(channelToRemove,profileId ))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("Profile [" + profileId.id() +"] not found");
        }
    }

    @Nested
    class SettingsLifecycle {
        @Test
        void createNotificationSettings_savesNewSettings() {
            var profileId = UUID.randomUUID();

            service.createNotificationSettings(profileId);

            var captor = ArgumentCaptor.forClass(NotificationSettings.class);
            verify(notificationSettingsRepository).save(captor.capture());
            assertThat(captor.getValue().getProfileId().id()).isEqualTo(profileId);
        }

        @Test
        void getNotificationSettings_returnsDto() {
            var profileId = new ProfileId(UUID.randomUUID());
            var settings = new NotificationSettings(profileId);

            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.of(settings));

            var result = service.getNotificationSettings(profileId);

            assertThat(result.channels()).isEqualTo(settings.getChannelType());
        }
    }
    
    @Nested
    class ExceptionFlow {

        @Test
        void getNotificationSettings_profileNotFound_throwsNotFoundException() {
            var profileId = new ProfileId(UUID.randomUUID());
            when(notificationSettingsRepository.findById(profileId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getNotificationSettings(profileId))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}