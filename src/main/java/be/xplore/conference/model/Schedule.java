package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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
    private List<Room> rooms;
    @OneToMany
    private List<Talk> talks;

    public Schedule(LocalDate date, DayOfWeek day) {
        this.date = date;
        this.day = day;
    }
}
