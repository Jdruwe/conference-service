package be.xplore.conference.notifications;

import be.xplore.conference.model.Admin;
import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import be.xplore.conference.service.AdminService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailSenderTest {

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private AdminService adminService;

    @Autowired
    private EmailSender emailSender;

    private List<Client> clients;

    @Before
    public void setUp() {
        LocalDateTime date = LocalDateTime.now();
        Room room = Room.builder()
                .name("Test room")
                .build();

        Admin admin = Admin.builder()
                .email("bob@admin.com")
                .build();

        Mockito.when(adminService.loadAllAdmins())
                .thenReturn(Collections.singletonList(admin));

        clients = new ArrayList<>();
        clients.add(new Client(room, date));
    }

    @Test
    public void sendEmailForOfflineClientsOfflineClientsEmailsSend() {
        emailSender.sendEmailForOfflineClients(clients);
        Mockito.verify(javaMailSender).send(getExpectedOfflineMail());
    }


    @Test
    public void sendEmailForReconnectedClientsReconnectedClientEmailSend() {
        emailSender.sendEmailForReconnectedClient(clients.get(0));
        Mockito.verify(javaMailSender).send(getExpectedReconnectMail());
    }

    private SimpleMailMessage getExpectedOfflineMail() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Devoxx client update");
        mail.setTo("bob@admin.com");
        mail.setFrom("noReply@devoxx.com");
        mail.setText("Hello there,\n" +
                "\n" +
                "There are problems with following clients: Test room .\n" +
                "\n" +
                "Kind regards");
        return mail;
    }

    private SimpleMailMessage getExpectedReconnectMail() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Devoxx client reconnected");
        mail.setTo("bob@admin.com");
        mail.setFrom("noReply@devoxx.com");
        mail.setText("Hello there,\n" +
                "\n" +
                "Client for room: Test room came back online!\n" +
                "\n" +
                "Kind regards");
        return mail;
    }

}
