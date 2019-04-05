package be.xplore.conference.rest.dto;

import be.xplore.conference.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private LocalDate date;
    private DayOfWeek day;
    private RoomScheduleDto room;
}
