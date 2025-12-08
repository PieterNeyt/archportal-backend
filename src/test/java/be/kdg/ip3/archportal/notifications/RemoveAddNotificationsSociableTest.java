package be.kdg.ip3.archportal.notifications;

import be.kdg.ip3.archportal.communications.application.NotificationServices;
import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import be.kdg.ip3.archportal.communications.domain.notification.*;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.communications.shared.NotificationType;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
        import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class RemoveAddNotificationsSociableTest {

    @Mock
    NotificationSettingsRepository settingsRepository;
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    ProfilesApi profilesApi;
    @Mock
    JavaMailSender mailSender;
    @Mock
    SimpleMailMessage template;

    NotificationServices service;

    @BeforeEach
    void setUp() {
        service = new NotificationServices(mailSender, template, settingsRepository, notificationRepository, profilesApi);
    }

    @Nested
    class AddNotificationTests {

        @Test
        void addNotification_existingProfile_sendsEmailAndSavesNotification() {
            // Arrange
            var profileId = new ProfileId(UUID.randomUUID());
            var event = new AddNotificationEvent(
                    profileId.id(),
                    "BODY",
                    "TYPE",
                    NotificationType.SYSTEM
            );

            var settings = new NotificationSettings(profileId);

            when(settingsRepository.findById(profileId)).thenReturn(Optional.of(settings));
            when(profilesApi.getProfileEmail(profileId.id())).thenReturn("test@example.com");

            // Act
            service.addNotification(event);

            // Assert
            verify(mailSender).send(any(SimpleMailMessage.class));
            verify(notificationRepository).save(any(Notification.class));
        }

        @Test
        void addNotification_profileNotFound_createsSettingsAndSavesNotification() {
            // Arrange
            var profileId = new ProfileId(UUID.randomUUID());
            var event = new AddNotificationEvent(
                    profileId.id(),
                    "BODY",
                    "TYPE",
                    NotificationType.SYSTEM
            );

            when(settingsRepository.findById(profileId)).thenReturn(Optional.empty());
            when(profilesApi.getProfileEmail(profileId.id())).thenReturn("test@example.com");

            // Act
            service.addNotification(event);

            // Assert
            verify(mailSender).send(any(SimpleMailMessage.class));
            verify(notificationRepository).save(any(Notification.class));
        }
    }

    @Nested
    class RemoveNotificationTests {

        @Test
        void readNotification_existingNotification_deletesIt() {
            // Arrange
            var notificationId = new NotificationId(UUID.randomUUID());
            var receiverId = new ReceiverId(UUID.randomUUID());

            var notification = mock(Notification.class);
            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

            // Act
            service.readNotification(receiverId, notificationId);

            // Assert
            verify(notification).checkReceiver(receiverId);
            verify(notificationRepository).delete(notification);
        }

        @Test
        void readNotification_notificationNotFound_throwsNotFoundException() {
            // Arrange
            var notificationId = new NotificationId(UUID.randomUUID());
            var receiverId = new ReceiverId(UUID.randomUUID());

            when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.readNotification(receiverId, notificationId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Notificaiton [" + notificationId + "] not found");
        }
    }
}
