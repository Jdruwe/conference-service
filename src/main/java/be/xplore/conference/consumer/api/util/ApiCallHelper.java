package be.xplore.conference.consumer.api.util;

import be.xplore.conference.consumer.api.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
public class ApiCallHelper {
    private final ObjectMapper objectMapper;

    public ApiCallHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> ApiResponse queryApi(String url, String etag, Class<T> mapTo) throws IOException {
        log.error(url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET,
                createEtagHeader(etag),
                String.class);

        T mappedBody = mapBody(response.getBody(), mapTo);
        return new ApiResponse<>(response.getHeaders().getETag(), mappedBody);
    }

    private <T> T mapBody(String body, Class<T> mapTo) throws IOException {
        if (Objects.nonNull(body)) {
            return objectMapper.readValue(body, mapTo);
        }
        return null;
    }

    private HttpEntity<String> createEtagHeader(String etag) {
        HttpHeaders headers = new HttpHeaders();
        if (!"".equals(etag)) {
            headers.setIfNoneMatch(etag);
        }
        return new HttpEntity<>("parameters", headers);
    }
}
