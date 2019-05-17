package be.xplore.conference.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "devoxx.api")
public class DevoxxApiProperties {
    private String baseUrl;
    private String rooms;
    private String speaker;
}
