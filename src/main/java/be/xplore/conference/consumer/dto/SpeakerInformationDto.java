package be.xplore.conference.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerInformationDto {
    private String uuid;
    private String lastName;
    private String firstName;
    private String avatarURL;
    private String twitter;
}
