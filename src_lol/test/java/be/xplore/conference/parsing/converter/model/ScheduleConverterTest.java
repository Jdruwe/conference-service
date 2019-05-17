package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.DayOfWeek;
import be.xplore.conference.model.schedule.Schedule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ScheduleConverterTest {

    @Test
    public void testScheduleConversion() {
        LocalDate localDate = LocalDate.of(2018, 11, 12);
        Schedule schedule = ScheduleConverter.toSchedule(localDate, DayOfWeek.THURSDAY);
        Assert.assertNotNull(schedule);
        Assert.assertEquals(localDate, schedule.getDate());
        Assert.assertEquals(DayOfWeek.THURSDAY, schedule.getDay());
    }

}
