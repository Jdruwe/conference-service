package be.xplore.conference.notifications;

import be.xplore.conference.model.Admin;
import be.xplore.conference.model.Client;
import be.xplore.conference.service.AdminService;
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
    private AdminService adminService;

    @Autowired
    public EmailSender(JavaMailSender mailSender, AdminService adminService) {
        this.mailSender = mailSender;
        this.adminService = adminService;
    }

    public void sendEmailForOfflineClients(List<Client> clients) {
        if (clients.size() != 0) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject("Devoxx client update");
            mail.setText(setBodyForOfflineMail(clients).toString());
            mail.setFrom("noReply@devoxx.com");
            sendMailToAllAdmins(mail);
            log.info("Mail for offline clients send");
        }
    }

    public void sendEmailForReconnectedClient(Client client) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Devoxx client reconnected");
        mail.setText(setBodyForReconnectMail(client).toString());
        mail.setFrom("noReply@devoxx.com");
        sendMailToAllAdmins(mail);
        log.info("Mail for reconnected client send");
    }

    private void sendMailToAllAdmins(SimpleMailMessage mailMessage){
        for (Admin admin: adminService.loadAllAdmins()) {
            mailMessage.setTo(admin.getEmail());
            mailSender.send(mailMessage);
        }
    }

    private StringBuilder setBodyForOfflineMail(List<Client> clients){
        StringBuilder textForMail = new StringBuilder();
        textForMail.append("Dear sir/madam,\n\n");
        textForMail.append("There are problems with these clients: ");
        for (Client client : clients) {
            textForMail.append(client.getRoom().getName()).append(", ");
        }
        textForMail.deleteCharAt(textForMail.length() - 2);
        textForMail.append(".");
        textForMail.append("\n\nKind regards");
        return textForMail;
    }

    private StringBuilder setBodyForReconnectMail(Client client){
        StringBuilder textForMail = new StringBuilder();
        textForMail.append("Beste,\n\n");
        textForMail.append("Client van room: ").append(client.getRoom().getName()).append(" is terug online gekomen.");
        textForMail.append("\n\nKind regards");
        return textForMail;
    }

}
