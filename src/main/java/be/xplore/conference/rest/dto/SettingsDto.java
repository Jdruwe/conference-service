package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SettingsDto {
    private int minutesBeforeNextSession;
    private boolean isRoomOccupancyOn;
}
