package be.xplore.conference.consumer.helper.dto;

import be.xplore.conference.consumer.dto.SpeakerInformationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerResponse {
    private String etag;
    private SpeakerInformationDto speakerInformation;
}
