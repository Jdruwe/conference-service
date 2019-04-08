package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSettingsDto {
    private int minutesBeforeNextSession;
    private boolean isRoomOccupancyOn;

}
