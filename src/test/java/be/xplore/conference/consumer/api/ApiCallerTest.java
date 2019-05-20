package be.xplore.conference.consumer.api;

import be.xplore.conference.consumer.api.dto.RoomScheduleResponse;
import be.xplore.conference.consumer.api.dto.RoomsResponse;
import be.xplore.conference.consumer.api.dto.SpeakerResponse;
import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.consumer.dto.SpeakerInformationDto;
import be.xplore.conference.model.DayOfWeek;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ApiCallerTest {

    private static final String ETAG = "v2-791456269257604";
    private static final String PATHROOM5TUESDAY = "/api/conferences/dvbe18/rooms/Room5/tuesday";
    private static final String ROOM5 = "Room5";
    private static final String GETLOCALHOST = "localhost";
    private static final String GET = "GET";
    private static final int PORT = 9999;

    @Autowired
    private ApiCaller apiCaller;

    private ClientAndServer mockServer;

    @Before
    public void startMockServer() {
        mockServer = startClientAndServer(PORT);
    }

    @After
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void testApiCallIsMadeOnce() {
        new MockServerClient(GETLOCALHOST, PORT)
                .when(request().withMethod(GET).withPath(PATHROOM5TUESDAY))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, ETAG)
                        .withBody(readFromClasspath("tuesday-room-5.json")));

        apiCaller.getRoomSchedule(ROOM5, null, DayOfWeek.TUESDAY);

        new MockServerClient(GETLOCALHOST, PORT)
                .verify(request()
                                .withPath(PATHROOM5TUESDAY),
                        VerificationTimes.once());
    }

    @Test
    public void testApiCallIsMadeAtLeastTwice() {
        new MockServerClient(GETLOCALHOST, PORT)
                .when(request().withMethod(GET).withPath(PATHROOM5TUESDAY))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, ETAG)
                        .withBody(readFromClasspath("tuesday-room-5.json")));

        apiCaller.getRoomSchedule(ROOM5, null, DayOfWeek.TUESDAY);
        apiCaller.getRoomSchedule(ROOM5, null, DayOfWeek.TUESDAY);

        new MockServerClient(GETLOCALHOST, PORT)
                .verify(
                        request()
                                .withPath(PATHROOM5TUESDAY),
                        VerificationTimes.atLeast(2));
    }

    @Test
    public void testGetRooms() {
        new MockServerClient(GETLOCALHOST, PORT)
                .when(request().withMethod(GET).withPath("/api/conferences/dvbe18/rooms/"))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, ETAG)
                        .withBody(readFromClasspath("rooms.json")));

        RoomsResponse response = apiCaller.getRooms(null);

        assertThat(response).isNotNull().satisfies(r -> {
            assertThat(r.getEtag()).isEqualTo(ETAG);
            assertThat(r.getRooms()).isNotNull().satisfies(schedule -> {
                assertThat(schedule.getRooms()).hasSize(11);
                assertThat(schedule).isInstanceOfAny(RoomsDto.class);
            });
        });
    }

    @Test
    public void testGetRoomSchedule() {
        new MockServerClient(GETLOCALHOST, PORT)
                .when(request().withMethod(GET).withPath(PATHROOM5TUESDAY))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, ETAG)
                        .withBody(readFromClasspath("tuesday-room-5.json")));

        RoomScheduleResponse response = apiCaller.getRoomSchedule(ROOM5, null, DayOfWeek.TUESDAY);

        assertThat(response).isNotNull().satisfies(r -> {
            assertThat(r.getEtag()).isEqualTo(ETAG);
            assertThat(r.getSchedule()).isNotNull().satisfies(schedule -> {
                assertThat(schedule.getSlots()).hasSize(5);
                assertThat(schedule).isInstanceOfAny(ScheduleDto.class);
            });
        });
    }

    @Test
    public void testGetSpeaker() {
        new MockServerClient(GETLOCALHOST, PORT)
                .when(request().withMethod(GET).withPath("/api/conferences/dvbe18/speakers/f6be30c224fa250ab01c56d11b043036b51f4599"))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader(HttpHeaders.ETAG, ETAG)
                        .withBody(readFromClasspath("speaker.json")));

        SpeakerResponse response = apiCaller.getSpeaker("f6be30c224fa250ab01c56d11b043036b51f4599", null);

        assertThat(response).isNotNull().satisfies(r -> {
            assertThat(r.getEtag()).isEqualTo(ETAG);
            assertThat(r.getSpeakerInformation()).isNotNull().satisfies(speakerInformation -> {
                Assert.assertEquals(speakerInformation.getCompany(), "Lightbend Inc.");
                Assert.assertEquals(speakerInformation.getUuid(), "f6be30c224fa250ab01c56d11b043036b51f4599");
                assertThat(speakerInformation).isInstanceOfAny(SpeakerInformationDto.class);
            });
        });
    }

    private String readFromClasspath(String filename) {
        try (InputStream in = new ClassPathResource(filename, getClass()).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to read file from classpath %s %s", filename, e.getMessage()), e);
        }
    }
}
