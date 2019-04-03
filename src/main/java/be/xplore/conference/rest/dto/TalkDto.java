package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalkDto {
    private Date startTime;
    private Date endTime;
    private String fromTime;
    private String toTime;
    private String title;
    private String type;
    private String summary;
    private List<SpeakerDto> speakers;
}
