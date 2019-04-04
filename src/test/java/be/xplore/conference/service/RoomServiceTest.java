package be.xplore.conference.service;

import be.xplore.conference.model.Room;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoomServiceTest {

/*    @Autowired
    private RoomService roomService;

    private Room room1;
    private Room room2;

    @Before
    public void setUp() {
        room1 = new Room("room1", "room 1", 105, "conference");
        room2 = new Room("room2", "room 2", 245, "talk");
    }

    @Test
    public void testSaveRoom() {
        roomService.save(room1);
        roomService.save(room2);
        int amountAfter = roomService.loadAll().size();
        Assert.assertEquals(2, amountAfter);
    }

    @Test
    public void testLoadAllRoomsWhenThereAreNon() {
        int amount = roomService.loadAll().size();
        Assert.assertEquals(0, amount);
    }*/


}
