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
public class RoomScheduleDto {
    private String id;
    private String name;
    private LocalDate date;
    private DaysOfTheWeek day;
    private List<TalkDto> talks;
}
