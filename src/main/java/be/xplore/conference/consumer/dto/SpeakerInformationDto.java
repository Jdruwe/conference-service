package be.xplore.conference.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerInformationDto {
    private String uuid;
    private String bioAsHtml;
    @JsonIgnore
    private Object AcceptedTalks;
    private String company;
    private String bio;
    private String lastName;
    private String firstName;
    private String blog;
    private String avatarURL;
    private String twitter;
    private String lang;
}
