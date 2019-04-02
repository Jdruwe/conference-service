package be.xplore.conference.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class devoxxConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(devoxxConsumer.class);

    final String url = "https://dvbe18.confinabox.com/api";

    @PostConstruct
    private void getConfernceDays() {
        LOGGER.info("kek");
        final String uri = "http://dvbe18.confinabox.com/api/conferences/dvbe18/proposalTypes";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        LOGGER.warn(response.toString());
    }
}
