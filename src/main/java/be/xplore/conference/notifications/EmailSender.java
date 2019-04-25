package be.xplore.conference.notifications;

import be.xplore.conference.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailSender {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(List<Client> clients) {

        if (clients.size() != 0) {
            StringBuilder textForMail = new StringBuilder("");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("pieterjan.noe@gmail.com");
            mail.setSubject("Devoxx client update");
            for (Client client : clients) {
                textForMail.append("Er is een client in room: ").append(client.getRoom().getName()).append(" waar iets mis mee is.\n");
            }
            mail.setText(textForMail.toString());
            mail.setFrom("noReply@devoxx.com");
            mailSender.send(mail);
        }
    }
}
