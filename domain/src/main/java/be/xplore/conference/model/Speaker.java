package be.xplore.conference.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Speaker {
    @Id
    private String uuid;
    private String firstName;
    private String lastName;
    @Column(length = 10000)
    private String avatarUrl;
    private String twitter;
    private String etag;

}
