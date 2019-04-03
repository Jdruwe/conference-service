package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    private String id_room;
    private String name;
    private int capacity;
    private String setup;

    @OneToMany
    private List<Talk> talks;

    public Room(String id, String name, int capacity, String setup) {
        this.id_room = id;
        this.name = name;
        this.capacity = capacity;
        this.setup = setup;
    }
}
