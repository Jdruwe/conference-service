package be.xplore.conference.service;

import be.xplore.conference.excpetion.ScheduleNotFoundException;
import be.xplore.conference.model.Schedule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void testLoadByDateAndRoom() throws ScheduleNotFoundException {
        LocalDate date = LocalDate.of(2018, 11, 12);
        Schedule schedule = scheduleService.loadByDateAndRoom(date, "Room8")
                .orElseThrow(ScheduleNotFoundException::new);
        Assert.assertNotNull(schedule);
        Assert.assertEquals(date, schedule.getDate());
    }

}
