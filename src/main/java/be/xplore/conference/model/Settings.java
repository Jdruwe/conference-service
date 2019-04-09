package be.xplore.conference.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@ToString
@Builder
public class Settings {
    @Id
    private String key;
    private String value;

    public Settings(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
