package be.kdg.ip3.archportal.communications.application;


import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationId;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationRepository;
import be.kdg.ip3.archportal.communications.domain.notification.ReceiverId;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettings;
import be.kdg.ip3.archportal.communications.domain.settings.NotificationSettingsRepository;
import be.kdg.ip3.archportal.communications.domain.settings.ProfileId;
import be.kdg.ip3.archportal.communications.shared.AddNotificationEvent;
import be.kdg.ip3.archportal.profiles.shared.ProfilesApi;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServices {
    private final JavaMailSender mailSender;
    private final SimpleMailMessage template;
    private final NotificationSettingsRepository settingsRepository;
    private final NotificationRepository repository;
    private final ProfilesApi profilesApi;

    public NotificationServices(JavaMailSender mailSender,
                                SimpleMailMessage template
            , NotificationSettingsRepository settingsRepository, NotificationRepository repository, ProfilesApi profilesApi) {
        this.mailSender = mailSender;
        this.template = template;
        this.settingsRepository = settingsRepository;
        this.repository = repository;
        this.profilesApi = profilesApi;
    }

    public void sendEmailNotification(Notification notification) {
        var message = new SimpleMailMessage(template);

        var recieverEmail = profilesApi.getProfileEmail(notification.getReceiverId().id());

        message.setTo(recieverEmail);

        message.setSubject(notification.getTitle());
        message.setText(notification.getBody());
        message.setSentDate(notification.getCreatedAt());

        mailSender.send(message);
    }

    public void addNotification(AddNotificationEvent newNotificationEvent) {
        var profileId = new ProfileId(newNotificationEvent.recieverId());
        var settings = settingsRepository.findById(profileId)
                .orElse(new NotificationSettings(profileId));

        var newNotification = new Notification(
                newNotificationEvent.type(),
                newNotificationEvent.body(),
                newNotificationEvent.title(),
                new ReceiverId(profileId.id()));

        if (settings.containsEmail()) {
            sendEmailNotification(newNotification);
        }

        if (settings.containsInPlatform()) {
            repository.save(newNotification);
        }
    }

    public List<Notification> getNotifications(ReceiverId receiverId) {

        return repository.findByReceiverId(receiverId)
                .orElseThrow(() -> new NotFoundException("No notifications found for current reciever"));
    }

    public List<Notification> getFirstAmountNotifications(ReceiverId receiverId, int amount) {
        return repository.findByReceiverIdFirstAmount(receiverId,amount)
                .orElseThrow(() -> new NotFoundException("No notifications found for current reciever"));
    }

    public int getTotalNotifications(ReceiverId receiverId) {
        return repository.getTotalNotificationFromReceiverId(receiverId);
    }

    public void readNotification(ReceiverId receiverId, NotificationId notificationId) {
        var notification = repository.findById(notificationId)
                .orElseThrow(notificationId::notFound);

        notification.checkReciever(receiverId);
        this.repository.delete(notification);
    }
}

