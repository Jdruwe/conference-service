package be.xplore.conference.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakDto {
    private String id;
    private String nameEN;
    private String nameFR;
    private BreakRoomDto room;
    private String dayName;
    private long startTime;
    private long endTime;

}
