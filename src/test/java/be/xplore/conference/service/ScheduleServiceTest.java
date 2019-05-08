package be.xplore.conference.service;

import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.Schedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;

    private Schedule schedule;
    private LocalDate date;

    @Before
    public void setUp() {
        date = LocalDate.of(2018,11,13);
        schedule = new Schedule(date, DayOfWeek.TUESDAY);
    }

    @Test
    public void testSaveSchedule() {
        scheduleService.save(schedule);
        Assert.assertTrue(scheduleService.loadById(date).isPresent());
    }

    @Test
    public void testLoadScheduleById() {
        scheduleService.save(schedule);
        Optional<Schedule> loadedSchedule = scheduleService.loadById(date);
        Assert.assertTrue(loadedSchedule.isPresent());
        Assert.assertEquals(schedule,loadedSchedule.get());
    }

}
