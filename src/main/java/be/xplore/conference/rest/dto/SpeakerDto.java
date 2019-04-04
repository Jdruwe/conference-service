package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeakerDto {
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String twitter;
}
