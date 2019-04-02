package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    String id;
    String name;
    int capacity;
    String setup;
    List<Schedule> schedules;
}
