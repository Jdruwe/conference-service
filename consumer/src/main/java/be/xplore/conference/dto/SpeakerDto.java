package be.xplore.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerDto {
    private LinkDto link;
    private String name;
}
