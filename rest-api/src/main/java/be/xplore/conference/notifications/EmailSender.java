package be.xplore.conference.notifications;

import be.xplore.conference.Client;
import be.xplore.conference.model.auth.Admin;
import be.xplore.conference.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailSender {
    private final JavaMailSender mailSender;
    private final AdminService adminService;

    @Autowired
    public EmailSender(JavaMailSender mailSender, AdminService adminService) {
        this.mailSender = mailSender;
        this.adminService = adminService;
    }

    public void sendEmailForOfflineClients(List<Client> clients) {
        if (!clients.isEmpty()) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject("Devoxx client update");
            mail.setText(createBodyForOfflineMail(clients).toString());
            mail.setFrom("noReply@devoxx.com");
            sendMailToAllAdmins(mail);
        }
    }

    public void sendEmailForReconnectedClient(Client client) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Devoxx client reconnected");
        mail.setText(createBodyForReconnectMail(client).toString());
        mail.setFrom("noReply@devoxx.com");
        sendMailToAllAdmins(mail);
    }

    private void sendMailToAllAdmins(SimpleMailMessage mailMessage) {
        for (Admin admin : adminService.loadAllAdmins()) {
            mailMessage.setTo(admin.getEmail());
            mailSender.send(mailMessage);
        }
    }

    private StringBuilder createBodyForOfflineMail(List<Client> clients) {
        StringBuilder textForMail = new StringBuilder();
        textForMail.append("Hello there,\n\n")
                .append("There are problems with following clients: ");
        for (Client client : clients) {
            textForMail.append(client.getRoom().getName())
                    .append(", ");
        }
        textForMail.deleteCharAt(textForMail.length() - 2);
        textForMail.append('.')
                .append("\n\nKind regards");
        return textForMail;
    }

    private StringBuilder createBodyForReconnectMail(Client client) {
        StringBuilder textForMail = new StringBuilder();
        textForMail.append("Hello there,\n\n")
                .append("Client for room: ")
                .append(client.getRoom().getName())
                .append(" came back online!")
                .append("\n\nKind regards");
        return textForMail;
    }

}
