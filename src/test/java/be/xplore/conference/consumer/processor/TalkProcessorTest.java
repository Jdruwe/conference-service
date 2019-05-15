
package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.SlotDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TalkProcessorTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TalkProcessor talkProcessor;


    private ClientAndServer mockServer;
    private int port = 9999;

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(port);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void testProcessTalks() throws IOException {
        new MockServerClient("localhost", port)
                .when(request().withMethod("GET").withPath("/api/conferences/dvbe18/speakers/.*"))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, "v2-791456269257604")
                        .withBody(readFromClasspath("speaker.json")));

        String textForObject = readFromClasspath("slots.json");
        List<SlotDto> slotDtoObjects = objectMapper.readValue(textForObject, new TypeReference<List<SlotDto>>() {});

        assertThat(talkProcessor.process(slotDtoObjects)).isNotNull().satisfies(p -> {
            assertThat(p).hasSize(5);
            assertThat(p.get(0).getSpeakers()).isNotNull().satisfies(speaker -> {
                assertThat(speaker).hasSize(2);
                assertEquals("c36efd6e34dbfa7e4af7869ec4132248215cb717", speaker.get(0).getUuid());
            });
        });
    }

    private String readFromClasspath(String filename) {
        try (InputStream in = new ClassPathResource(filename, getClass()).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to read file from classpath %s", filename), e);
        }
    }
}
