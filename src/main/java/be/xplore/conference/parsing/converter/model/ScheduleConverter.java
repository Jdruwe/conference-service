package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.Schedule;

import java.time.LocalDate;

public class ScheduleConverter {
    public static Schedule toSchedule(LocalDate date, DayOfWeek dayOfWeek) {
        return new Schedule(date, dayOfWeek);
    }
}
