package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RoomScheduleId implements Serializable {

    @ManyToOne
    private Schedule schedule;

    @OneToOne
    private Room room;
}
