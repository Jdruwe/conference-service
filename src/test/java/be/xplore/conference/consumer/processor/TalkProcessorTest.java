
package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.SlotDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TalkProcessorTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TalkProcessor talkProcessor;

    @Test
    public void testtest() throws IOException {
        String textForObject = readFromClasspath("SlotDto.json");
        List<SlotDto> myObjects = objectMapper.readValue(textForObject, new TypeReference<List<SlotDto>>() {});
        log.error(myObjects.toString());
        talkProcessor.process(myObjects);
    }

    private String readFromClasspath(String filename) {
        try (InputStream in = new ClassPathResource(filename, getClass()).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to read file from classpath %s", filename), e);
        }
    }
}
