package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    private Room room;
    private Date lastConnected;

    public Client(Room room, Date lastConnected) {
        this.room = room;
        this.lastConnected = lastConnected;
    }
}
