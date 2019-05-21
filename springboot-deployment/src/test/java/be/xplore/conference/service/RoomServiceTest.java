package be.xplore.conference.service;

import be.xplore.conference.model.Room;
import be.xplore.conference.model.schedule.RoomSchedule;
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
        room1 = Room.builder()
                .id("room1")
                .name("room 1")
                .capacity(105)
                .setup("conference")
                .roomSchedule(new RoomSchedule(null)).build();
    }

    @Test
    public void testLoadAllRoomsWhenThereAreNon() {
        int amount = roomService.loadAll().size();
        Assert.assertEquals(0, amount);
    }

    @Test
    public void testSaveRoom() {
        roomService.save(room1);
        Assert.assertTrue(roomService.loadById("room1").isPresent());
    }
}
