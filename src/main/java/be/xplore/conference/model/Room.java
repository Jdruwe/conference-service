package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Builder
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

}
