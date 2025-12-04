package be.kdg.ip3.archportal.communications.application;


import be.kdg.ip3.archportal.communications.domain.NotFoundException;
import be.kdg.ip3.archportal.communications.domain.notification.Notification;
import be.kdg.ip3.archportal.communications.domain.notification.NotificationRepository;
import be.kdg.ip3.archportal.communications.domain.notification.RecieverId;
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
        SimpleMailMessage message = new SimpleMailMessage(template);

        String recieverEmail = profilesApi.getProfileEmail(notification.getRecieverId().id());

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
                new RecieverId(profileId.id()));

        if (settings.containsEmail()) {
            sendEmailNotification(newNotification);
        }

        if (settings.containsInPlatform()) {
            repository.save(newNotification);
        }
    }

    public List<Notification> getNotifications(RecieverId recieverId) {

        return repository.findByRecieverId(recieverId)
                .orElseThrow(() -> new NotFoundException("No notifications found for current reciever"));
    }

    public List<Notification> getFirst5Notifications(RecieverId recieverId) {
        return repository.findByRecieverIdFirst5(recieverId)
                .orElseThrow(() -> new NotFoundException("No notifications found for current reciever"));
    }
}

