package be.xplore.conference.api.dto;

import be.xplore.conference.dto.SpeakerInformationDto;
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
