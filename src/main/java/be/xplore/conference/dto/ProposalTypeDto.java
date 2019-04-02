package be.xplore.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProposalTypeDto {
    private String id;
    private String description;
    private String label;
    private int duration;

}
