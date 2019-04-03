package be.xplore.conference.rest.dto;

import be.xplore.conference.model.DaysOfTheWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private DaysOfTheWeek day;
    private LocalDate date;

    private List<RoomScheduleDto> rooms;
}
