package be.xplore.conference.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalkDto {
    private String trackId;
    private String talkType;
    private String track;
    private String audienceLevel;
    @JsonIgnore
    private String summaryAsHtml;
    private String id;
    private List<SpeakerDto> speakers;
    private String title;
    private String lang;
    private String summary;
}
