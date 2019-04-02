package be.xplore.conference.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProposalTypesDto implements Serializable {
    private String content;
    private List<ProposalTypeDto> proposalTypes;

}
