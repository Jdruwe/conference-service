package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.model.DaysOfTheWeek;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class DevoxxConsumer {
    @Value("${devoxx.api.url}")
    private String apiUrl;
    @Value("${devoxx.schedule.api.url}")
    private String scheduleUrl;

    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(DevoxxConsumer.class);

    public DevoxxConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void getSchedule() throws IOException {
        for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
            String url = apiUrl + scheduleUrl + "/" + day.name().toLowerCase();

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            ScheduleDto schedule = objectMapper.readValue(result, ScheduleDto.class);
            
            LOGGER.info(String.valueOf(schedule.getSlots().size()));
            LOGGER.info(schedule.getSlots().get(0).getDay());
        }
    }
}
