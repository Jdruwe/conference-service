package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDto {
    private int minutesBeforeNextSession;
    private int mailDelayForConnectionIssues;
    private boolean isRoomOccupancyOn;
    private boolean showMessage;
    private String message;
}
