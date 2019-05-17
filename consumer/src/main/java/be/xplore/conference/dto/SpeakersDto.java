package be.xplore.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakersDto {
    private List<SpeakerDto> speakerDtos;
}
