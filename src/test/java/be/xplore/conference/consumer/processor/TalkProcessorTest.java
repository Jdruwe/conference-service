package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.api.ApiCaller;
import be.xplore.conference.consumer.api.dto.RoomScheduleResponse;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.model.DayOfWeek;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TalkProcessorTest {

    @Autowired
    private TalkProcessor talkProcessor;

    @Autowired
    private ApiCaller apiCaller;


    private WireMockServer wireMockServer = new WireMockServer();


    @Before
    public void startServer() {
        wireMockServer.start();
    }

    @After
    public void stopServer() {
        wireMockServer.stop();
    }

    @Test
    public void testTalkProcessing() {
        //configureFor("https://dvbe18.confinabox.com/api/conferences/dvbe18", 8080); https://dvbe18.confinabox.com/api/conferences/dvbe18/rooms/Room5/tuesday
        stubFor(get(urlEqualTo("https://dvbe18.confinabox.com/api/conferences/dvbe18/rooms/Room5/tuesday")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("Hello world!")));
        RoomScheduleResponse response = apiCaller.getRoomSchedule("Room5",
                null,
                DayOfWeek.TUESDAY);
        ScheduleDto scheduleDto = response.getSchedule();
        talkProcessor.process(scheduleDto.getSlots());
    }
}
