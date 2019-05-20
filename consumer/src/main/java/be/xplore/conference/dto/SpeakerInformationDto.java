package be.xplore.conference.dto;

import be.xplore.conference.model.Speaker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeakerInformationDto {
    private String uuid;
    private String bioAsHtml;
    @JsonIgnore
    private Object acceptedTalks;
    private String company;
    private String bio;
    private String lastName;
    private String firstName;
    private String blog;
    private String avatarURL;
    private String twitter;
    private String lang;

    public Speaker toDomain() {
        return Speaker.builder()
                .uuid(uuid)
                .firstName(firstName)
                .lastName(lastName)
                .avatarUrl(avatarURL)
                .twitter(twitter)
                .build();
    }
}
