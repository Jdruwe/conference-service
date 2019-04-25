package be.xplore.conference.service;

import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomNotFoundException;
import be.xplore.conference.model.Client;
import be.xplore.conference.model.Room;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private RoomService roomService;

    private Room room;
    private Date date;

    @Before
    public void setUp() {
        room = new Room("testRoom", "Test room", 850, "setup");
        date = new Date();
        roomService.save(room);
    }

    @Test
    public void testSave() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Client client = new Client(room, date);
        Client savedClient = clientService.save(client);
        assertNotNull(savedClient);
        assertEquals(savedClient.getLastConnected(), date);
    }

    @Test(expected = RoomNotFoundException.class)
    public void testRegisterThrowsExceptionRoomNotFoundException() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Room emptyRoom = new Room("doesNotExist", "doesNotExist", 1, "...");
        Client client = new Client(emptyRoom, date);
        clientService.save(client);
    }

    @Test(expected = RoomAlreadyRegisteredException.class)
    public void testRegisterThrowsExceptionRoomAlreadyRegisteredException() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Client client = new Client(room, date);
        clientService.save(client);
        clientService.save(client);
    }

    @Test
    public void testDelete() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Client client = new Client(room, date);
        Client savedClient = clientService.save(client);
        int result = clientService.delete(savedClient.getId());
        assertTrue(result > 0);
        assertEquals(result, 1);
    }

    @Test
    public void testLoadAllClients() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Client client = new Client(room, date);
        clientService.save(client);
        List<Client> clients = clientService.loadAll();
        assertNotNull(clients);
        assertEquals(clients.size(), 1);
        assertEquals(clients.get(0).getRoom(), room);
    }

    @Test(expected = RoomNotFoundException.class)
    public void testUpdateLastConnectedTimeThrowsExceptionRoomNotFoundException() throws RoomAlreadyRegisteredException, RoomNotFoundException {
        Client client = new Client(
                new Room("doesNotExist", "doesNotExist", 1, "..."),
                date);
        clientService.save(client);
    }

    @Test
    public void testUpdateLastConnectedTime() throws RoomAlreadyRegisteredException, RoomNotFoundException, InterruptedException {
        Client client = new Client(room, date);
        //waiting to get time difference
        Thread.sleep(2000);
        Client client1 = clientService.save(client);
        Date newDate = new Date();
        Client updatedClient = clientService.updateLastConnectedTime(client1.getId(), newDate);
        assertEquals(updatedClient.getLastConnected(), newDate);
        assertNotEquals(updatedClient.getLastConnected(), date);
        assertEquals(updatedClient.getRoom(), room);
    }

}
