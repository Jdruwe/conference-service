package be.xplore.conference.service;

import be.xplore.conference.exception.RoomAlreadyRegisteredException;
import be.xplore.conference.exception.RoomNotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClientServiceTest {

    private static final String ROOM_ID_AND_NAME = "doesNotExist";

    @Autowired
    private ClientService clientService;
    @Autowired
    private RoomService roomService;

    private Room room;
    private LocalDateTime date;

    @Before
    public void setUp() {
        date = LocalDateTime.now();
        room = Room.builder()
                .id("testRoom")
                .name("Test room")
                .capacity(850)
                .setup("setup")
                .build();
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
        Room emptyRoom = Room.builder()
                .id(ROOM_ID_AND_NAME)
                .name(ROOM_ID_AND_NAME)
                .capacity(1)
                .setup("...")
                .build();
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
                Room.builder()
                        .id(ROOM_ID_AND_NAME)
                        .name(ROOM_ID_AND_NAME)
                        .capacity(1)
                        .setup("...")
                        .build(),
                date);
        clientService.save(client);
    }

    @Test
    public void testUpdateLastConnectedTime() throws RoomAlreadyRegisteredException, RoomNotFoundException, InterruptedException {
        Client client = new Client(room, date);
        //waiting to get time difference
        Thread.sleep(2000);
        Client client1 = clientService.save(client);
        LocalDateTime newDate = LocalDateTime.now();
        Client updatedClient = clientService.updateLastConnectedTime(client1.getId(), newDate);
        assertEquals(updatedClient.getLastConnected(), newDate);
        assertNotEquals(updatedClient.getLastConnected(), date);
        assertEquals(updatedClient.getRoom(), room);
    }

}
