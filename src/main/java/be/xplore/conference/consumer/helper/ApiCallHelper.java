package be.xplore.conference.consumer.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ApiCallHelper {

    private final ObjectMapper objectMapper;

    public ApiCallHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T queryApi(String url, Class<T> mapTo) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return objectMapper.readValue(result, mapTo);
    }
}
