package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Talk {
    String id;
    Date startTime;
    Date endTime;
    String fromTime;
    String tiTime;
    String title;
    String type;
    String summary;
}
