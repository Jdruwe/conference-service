package be.xplore.conference.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientHeartbeatDto {
    int clientId;
    LocalDateTime newDate;
}
