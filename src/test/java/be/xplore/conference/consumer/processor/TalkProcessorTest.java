
package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.api.ApiCaller;
import be.xplore.conference.consumer.api.dto.RoomScheduleResponse;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.model.DayOfWeek;
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
import java.util.Arrays;

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
    private TalkProcessor talkProcessor;

    @Autowired
    private ApiCaller apiCaller;

    private ClientAndServer mockServer;

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(1080);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }


/*
        RoomScheduleResponse expected = RoomScheduleResponse.builder()
                .etag("v2-791456269257604")
                .schedule(ScheduleDto.builder()
                        .slots(Arrays.asList(
                                new SlotDto(), new SlotDto(), new SlotDto(), new SlotDto(), new SlotDto()
                        )).build()).build();

        assertThat(response).isEqualToComparingFieldByFieldRecursively(expected);*/



    @Test
    public void testTalkProcessing() {


new MockServerClient("localhost", 1080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("rooms/Room5/tuesday")
                )
                .respond(
                        response().withBody("hello world!")
                );

        RoomScheduleResponse response = apiCaller.getRoomSchedule("Room5",
                null,
                DayOfWeek.TUESDAY);
        ScheduleDto scheduleDto = response.getSchedule();
        talkProcessor.process(scheduleDto.getSlots());
        new MockServerClient("localhost", 1080)
                // this request matcher matches every request
                .when(
                        request()
                )
                .respond(
                        response()
                                .withBody("some_response_body")
                );

    }

    @Test
    public void testApiCallIsMadeOnce() {
        new MockServerClient("localhost", 1080)
                .when(request().withMethod("GET").withPath("/api/conferences/dvbe18/rooms/Room5/tuesday"))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, "v2-791456269257604")
                        .withBody(readFromClasspath("tuesday-room-5.json")));

        RoomScheduleResponse response = apiCaller.getRoomSchedule("Room5", null, DayOfWeek.TUESDAY);

        assertThat(response).isNotNull().satisfies(r -> {
            assertThat(r.getEtag()).isEqualTo("v2-791456269257604");
            assertThat(r.getSchedule()).isNotNull().satisfies(schedule -> {
                assertThat(schedule.getSlots()).hasSize(5);
                assertThat(schedule).isInstanceOfAny(ScheduleDto.class);
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

/*
        RoomScheduleResponse expected = RoomScheduleResponse.builder()
                .etag("v2-791456269257604")
                .schedule(ScheduleDto.builder()
                        .slots(Arrays.asList(
                                new SlotDto(), new SlotDto(), new SlotDto(), new SlotDto(), new SlotDto()
                        )).build()).build();

        assertThat(response).isEqualToComparingFieldByFieldRecursively(expected);*/
}
