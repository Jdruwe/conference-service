package be.xplore.conference.service;

import be.xplore.conference.exception.RoomScheduleNotFoundException;
import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.RoomSchedule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
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

    @Test
    public void testLoadByDayAndRoom() throws RoomScheduleNotFoundException {
        RoomSchedule roomSchedule = service.loadByDayAndRoomId(DayOfWeek.WEDNESDAY, "Room8")
                .orElseThrow(RoomScheduleNotFoundException::new);
        Assert.assertNotNull(roomSchedule);
        Assert.assertEquals(java.time.DayOfWeek.WEDNESDAY, roomSchedule.getId().getSchedule().getDate().getDayOfWeek());
    }


    @Test
    public void testSaveRoomSchedule() throws RoomScheduleNotFoundException {
        LocalDate date = LocalDate.of(2018, 11, 12);
        RoomSchedule roomSchedule = service.loadByDateAndRoomId(date, "Room8").orElseThrow(RoomScheduleNotFoundException::new);
        roomSchedule.setEtag("testing");
        RoomSchedule savedRoomSchedule = service.save(roomSchedule);
        Assert.assertNotNull(savedRoomSchedule);
        Assert.assertEquals(roomSchedule.getEtag(),"testing");
    }
}

