package be.xplore.conference.rest.dto;

import be.xplore.conference.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomScheduleDto {
    private LocalDate date;
    private DayOfWeek day;
    private RoomDto room;
    private List<TalkDto> talks;
}
