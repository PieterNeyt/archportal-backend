package be.kdg.ip3.archportal.communications.application;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationServices {
    private final JavaMailSender mailSender;
    private final SimpleMailMessage template;

    public NotificationServices(JavaMailSender mailSender,
                                SimpleMailMessage template) {
        this.mailSender = mailSender;
        this.template = template;
    }


    public void sendNotification() {
            SimpleMailMessage message = new SimpleMailMessage(template);
            message.setTo("dorhugomc@gmail.com");
            message.setSubject("Test Mail van Spring Boot");
            message.setText("Dit is een testmail, verstuurd via Gmail SMTP.");

            mailSender.send(message);
    }
}

