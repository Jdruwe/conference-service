package be.xplore.conference.service;

import be.xplore.conference.model.Room;
import be.xplore.conference.model.RoomSchedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    private Room room1;

    @Before
    public void setUp() {
        room1 = new Room("room1", "room 1", 105, "conference", new RoomSchedule(null));
    }

    @Test
    public void testSaveRoom() {
        roomService.save(room1);
        Assert.assertTrue(roomService.loadById("room1").isPresent());
    }

    @Test
    public void testLoadAllRoomsWhenThereAreNon() {
        int amount = roomService.loadAll().size();
        Assert.assertEquals(11, amount);
    }
}
