package be.xplore.conference.consumer.api.util;

import be.xplore.conference.consumer.api.dto.ApiResponse;
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
    private static final Logger log = LoggerFactory.getLogger(ApiCallHelper.class);
    private final ObjectMapper objectMapper;

    public ApiCallHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> ApiResponse queryApi(String url, String etag, Class<T> mapTo) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET,
                createEtagHeader(etag),
                String.class);

        T mappedBody = mapBody(response.getBody(), mapTo);
        return new ApiResponse<>(response.getHeaders().getETag(), mappedBody);
    }

    private <T> T mapBody(String body, Class<T> mapTo) throws IOException {
        if (Objects.nonNull(body))
            return objectMapper.readValue(body, mapTo);
        return null;
    }

    private HttpEntity<String> createEtagHeader(String etag) {
        HttpHeaders headers = new HttpHeaders();
        if (Objects.nonNull(etag) && !etag.equals("")) {
            headers.setIfNoneMatch(etag);
        }
        return new HttpEntity<>("parameters", headers);
    }
}
