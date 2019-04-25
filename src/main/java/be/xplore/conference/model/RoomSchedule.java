package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RoomSchedule {

    @EmbeddedId
    private RoomScheduleId id;
    private String etag;

    @OneToMany
    private List<Talk> talks;

    public RoomSchedule(RoomScheduleId id) {
        this.id = id;
    }
}
