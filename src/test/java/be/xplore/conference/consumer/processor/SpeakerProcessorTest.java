package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.LinkDto;
import be.xplore.conference.consumer.dto.SpeakerDto;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SpeakerProcessorTest {


    @Autowired
    private SpeakerProcessor speakerProcessor;

    private ClientAndServer mockServer;

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(1080);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void testProcessSpeaker() {
        new MockServerClient("localhost", 1080)
                .when(request().withMethod("GET").withPath("/api/conferences/dvbe18/speakers/.*"))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, "v2-791456269257604")
                        .withBody(readFromClasspath("speaker.json")));


        List<SpeakerDto> speakerDtos = new ArrayList<>();
        speakerDtos.add(new SpeakerDto
                (new LinkDto
                        ("http://dvbe18.confinabox.com/api/conferences/dvbe18/speakers/05b9d537f1895a60adc4dbc25b6af2d1ef458854",
                                "http://dvbe18.confinabox.com/api/profile/speaker",
                                "Stephan Janssen"),
                        "Stephan Jannssen"));
        speakerDtos.add(new SpeakerDto
                (new LinkDto
                        ("http://dvbe18.confinabox.com/api/conferences/dvbe18/speakers/bd2f55b11bacf7aa2791921b48dd589c3567bc81",
                                "http://dvbe18.confinabox.com/api/profile/speaker",
                                "Mark Reinhold"),
                        "Mark Reinhold"));
        speakerProcessor.generateForTalk(speakerDtos);

        //TODO: write more asserts
        assertThat(speakerDtos).isNotNull();
    }

    private String readFromClasspath(String filename) {
        try (InputStream in = new ClassPathResource(filename, getClass()).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to read file from classpath %s", filename), e);
        }
    }

}
