package be.xplore.conference.consumer.helper;

import be.xplore.conference.consumer.helper.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

@Component
public class ApiCallHelper {

    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCallHelper.class);

    public ApiCallHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // todo refactor
    public <T> ApiResponse queryApi(String url, String etag, Class<T> mapTo) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        if (Objects.nonNull(etag) && !etag.equals("")) {
            headers.setIfNoneMatch(etag);
        }
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        LOGGER.info(response.getStatusCodeValue() + url);

        String responseBody = response.getBody();
        T mappedBody = null;
        if (Objects.nonNull(responseBody)) {
            mappedBody = objectMapper.readValue(response.getBody(), mapTo);
        }

        return new ApiResponse<>(response.getHeaders().getETag(), mappedBody);
    }
}
