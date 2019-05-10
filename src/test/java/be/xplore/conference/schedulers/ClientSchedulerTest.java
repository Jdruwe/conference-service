package be.xplore.conference.schedulers;

import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import be.xplore.conference.rest.dto.ClientInfoDto;
import be.xplore.conference.service.ClientService;
import be.xplore.conference.service.RoomService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClientSchedulerTest {

    @Autowired
    private ClientScheduler clientScheduler;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testCheckStatusClientsAndSendMail(){
        clientScheduler.checkStatusClientsAndSendMail();
        Assert.assertTrue(true);
    }

    @Test
    public void testWasClientOffline(){
        registerTestClients();
        List<Client> clients = clientService.loadAll();
        Assert.assertNotNull(clients);
        boolean wasClientOffline = clientScheduler.wasClientOffline(clients.get(0));
        Assert.assertFalse(wasClientOffline);
    }

    private void registerTestClients(){
        Room room = Room.builder()
                .id("testRoom")
                .name("Test room")
                .capacity(850)
                .setup("setup")
                .build();
        roomService.save(room);
        ZonedDateTime registeredDate = ZonedDateTime.now();
        ClientInfoDto clientInfoDto = new ClientInfoDto(room, registeredDate.toLocalDateTime());
        clientService.save(modelMapper.map(clientInfoDto, Client.class));
        clientService.save(modelMapper.map(clientInfoDto, Client.class));
        clientService.save(modelMapper.map(clientInfoDto, Client.class));
    }
}
