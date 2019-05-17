package be.xplore.conference.api.dto;

import be.xplore.conference.consumer.dto.RoomsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomsResponse {
    private String etag;
    private RoomsDto rooms;
}
