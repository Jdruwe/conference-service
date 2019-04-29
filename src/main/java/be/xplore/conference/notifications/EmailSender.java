package be.xplore.conference.notifications;

import be.xplore.conference.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailSender {
    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);
    private final JavaMailSender mailSender;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailForOfflineClients(List<Client> clients) {
        if (clients.size() != 0) {
            StringBuilder textForMail = new StringBuilder();
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("pieterjan.noe@gmail.com");
            mail.setSubject("Devoxx client update");
            textForMail.append("Beste,\n\n");
            textForMail.append("There are problems with these clients: ");
            for (Client client : clients) {
                textForMail.append(client.getRoom().getName()).append(", ");
            }
            textForMail.deleteCharAt(textForMail.length() - 2);
            textForMail.append(".");
            mail.setText(textForMail.toString());
            mail.setFrom("noReply@devoxx.com");
            mailSender.send(mail);
            log.info("Mail for offline clients send");
        }
    }

    public void sendEmailForReconnectedClient(Client client) {
        StringBuilder textForMail = new StringBuilder();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("pieterjan.noe@gmail.com");
        mail.setSubject("Devoxx client reconnected");
        textForMail.append("Beste,\n\n");
        textForMail.append("Client van room: ").append(client.getRoom().getName()).append(" is terug online gekomen.");
        mail.setText(textForMail.toString());
        mail.setFrom("noReply@devoxx.com");
        mailSender.send(mail);
        log.info("Mail for reconnected client send");
    }
}
