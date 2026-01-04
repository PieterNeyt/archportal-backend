package be.kdg.ip3.archportal.communications.notifications;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
                    .hasMessageContaining("Notificaiton [" + notificationId.id() + "] not found");
        }
    }

    @Nested
    class NotificationDeliveryLogic {
        @Test
        void addNotification_onlyInPlatformEnabled_doesNotSendEmail() {
            var profileId = new ProfileId(UUID.randomUUID());
            var event = new AddNotificationEvent(profileId.id(), "B", "T", NotificationType.CHAT);

            var settings = new NotificationSettings(profileId);
            settings.removeChannelType(ChannelType.EMAIL); // Only IN_PLATFORM remains

            when(settingsRepository.findById(profileId)).thenReturn(Optional.of(settings));

            service.addNotification(event);

            verify(notificationRepository).save(any());
            verify(mailSender, never()).send(any(SimpleMailMessage.class));
        }

        @Test
        void addNotification_nothingEnabled_doesNothing() {
            var profileId = new ProfileId(UUID.randomUUID());
            var event = new AddNotificationEvent(profileId.id(), "B", "T", NotificationType.CHAT);

            var settings = new NotificationSettings(profileId);
            settings.removeChannelType(ChannelType.EMAIL);
            settings.removeChannelType(ChannelType.IN_PLATFORM);

            when(settingsRepository.findById(profileId)).thenReturn(Optional.of(settings));

            service.addNotification(event);

            verify(notificationRepository, never()).save(any());
            verify(mailSender, never()).send(any(SimpleMailMessage.class));
        }
    }

    @Nested
    class NotificationRetrieval {
        @Test
        void getNotifications_returnsList() {
            var receiverId = new ReceiverId(UUID.randomUUID());
            var notifications = List.of(new Notification(NotificationType.CHAT, "B", "T", receiverId));

            when(notificationRepository.findByReceiverId(receiverId)).thenReturn(Optional.of(notifications));

            var result = service.getNotifications(receiverId);

            assertThat(result).hasSize(1);
        }

        @Test
        void getNotifications_noneFound_throwsNotFound() {
            var receiverId = new ReceiverId(UUID.randomUUID());
            when(notificationRepository.findByReceiverId(receiverId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getNotifications(receiverId))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class ExceptionFlows {
        @Test
        void readNotification_wrongReceiver_throwsIllegalArgumentException() {
            var realReceiver = new ReceiverId(UUID.randomUUID());
            var impostor = new ReceiverId(UUID.randomUUID());
            var notification = new Notification(NotificationType.SYSTEM, "Body", "Title", realReceiver);
            var notificationId = notification.getId();

            when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

            assertThatThrownBy(() -> service.readNotification(impostor, notificationId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("receiver id not match");

            verify(notificationRepository, never()).delete(any());
        }

        @Test
        void getNotifications_noNotificationsFound_throwsNotFoundException() {
            var receiverId = new ReceiverId(UUID.randomUUID());
            when(notificationRepository.findByReceiverId(receiverId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getNotifications(receiverId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("No notifications found");
        }
    }
}
