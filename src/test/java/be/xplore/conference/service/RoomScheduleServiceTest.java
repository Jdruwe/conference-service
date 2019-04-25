package be.xplore.conference.service;

import be.xplore.conference.excpetion.RoomScheduleNotFoundException;
import be.xplore.conference.model.RoomSchedule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RoomScheduleServiceTest {
    @Autowired
    private RoomScheduleService service;

    @Test
    public void testLoadByDateAndRoom() throws RoomScheduleNotFoundException {
        LocalDate date = LocalDate.of(2018, 11, 12);
        RoomSchedule roomSchedule = service.loadByDateAndRoomId(date, "Room8")
                .orElseThrow(RoomScheduleNotFoundException::new);
        Assert.assertNotNull(roomSchedule);
        Assert.assertEquals(date, roomSchedule.getId().getSchedule().getDate());
    }
}
