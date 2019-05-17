package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.DayOfWeek;
import be.xplore.conference.model.schedule.Schedule;

import java.time.LocalDate;

public class ScheduleConverter {
    public static Schedule toSchedule(LocalDate date, DayOfWeek dayOfWeek) {
        return new Schedule(date, dayOfWeek);
    }
}
