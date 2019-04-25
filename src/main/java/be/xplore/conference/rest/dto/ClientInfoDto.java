package be.xplore.conference.rest.dto;

import be.xplore.conference.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfoDto {
    private Room room;
    private Date lastConnected;
}
