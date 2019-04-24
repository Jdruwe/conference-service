package be.xplore.conference.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "settings")
public class SettingsProperties {
    private int minutesBeforeNextSession;
    private boolean isRoomOccupancyOn;
    private long queryRateInMilliseconds;
}
