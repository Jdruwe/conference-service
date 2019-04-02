package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Speaker {
    String uuid;
    String firstName;
    String lastName;
    String avatarUrl;
    String twitterHandle;
}
