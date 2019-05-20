package be.xplore.conference.api.dto;

import be.xplore.conference.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomScheduleResponse {
    private String etag;
    private ScheduleDto schedule;
}
