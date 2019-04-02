package be.xplore.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakRoomDto {
    private String id;
    private String name;
    private int capacity;
    private String setup;
    private String recorded;
}
