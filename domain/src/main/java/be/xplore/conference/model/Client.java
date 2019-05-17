package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private Room room;
    private LocalDateTime lastConnected;

    public Client(Room room, LocalDateTime lastConnected) {
        this.room = room;
        this.lastConnected = lastConnected;
    }
}
