package be.xplore.conference.consumer.helper.dto;

import be.xplore.conference.consumer.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomScheduleResponse {
    private String etag;
    private ScheduleDto schedule;
}
