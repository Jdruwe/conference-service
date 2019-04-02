package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Speaker {
    @Id
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String twitter;
}
