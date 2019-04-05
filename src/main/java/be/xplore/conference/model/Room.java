package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    private String id;
    private String name;
    private int capacity;
    private String setup;

    @OneToOne
    private RoomSchedule roomSchedule;

    public Room(String id, String name, int capacity, String setup) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.setup = setup;
    }
}
