package be.xplore.conference.model.schedule;

import be.xplore.conference.model.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Schedule {
    @Id
    private LocalDate date;
    @Column(unique = true)
    private DayOfWeek day;

    @ManyToMany
    private List<RoomSchedule> roomSchedules;

    public Schedule(LocalDate date, DayOfWeek day) {
        this.date = date;
        this.day = day;
    }
}
