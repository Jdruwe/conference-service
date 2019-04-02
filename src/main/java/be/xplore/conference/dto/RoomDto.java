package be.xplore.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private String roomId;
    private Boolean notAllocated;
    private long fromTimeMillis;
    private BreakDto breakDto;
    private String roomSetup;
    //    private TalkDto talk;
    private String fromTime;
    private long toTimeMillis;
    private String toTime;
    private int roomCapacity;
    private String roomName;
    private String slotId;
    private String day;
}
