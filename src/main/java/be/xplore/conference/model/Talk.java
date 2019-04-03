package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Talk {
    @Id
    private String id;
    private Date startTime;
    private Date endTime;
    private String fromTime;
    private String toTime;
    private String title;
    private String type;
    @Column(length = 10000)
    private String summary;

    @ManyToMany
    private List<Speaker> speakers;
}
