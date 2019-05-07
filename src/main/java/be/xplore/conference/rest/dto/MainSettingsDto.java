package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainSettingsDto {
    private int minutesBeforeNextSession;
    private boolean isRoomOccupancyOn;
}
