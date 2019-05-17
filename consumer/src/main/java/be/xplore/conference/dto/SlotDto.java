package be.xplore.conference.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotDto {
    private String roomId;
    private Boolean notAllocated;
    private long fromTimeMillis;
    @JsonProperty("break")
    private BreakDto breakDto;
    private String roomSetup;
    private TalkDto talk;
    private String fromTime;
    private long toTimeMillis;
    private String toTime;
    private int roomCapacity;
    private String roomName;
    private String slotId;
    private String day;
}
