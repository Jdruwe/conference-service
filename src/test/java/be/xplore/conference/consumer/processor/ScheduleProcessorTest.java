package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.service.ScheduleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ScheduleProcessorTest {

    @ClassRule
    public static final WireMockClassRule WIRE_MOCK = new WireMockClassRule(options()
            .fileSource(new SingleRootFileSource(Paths.get("src", "test", "resources", "wiremock").toFile()))
            .port(9999));

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleProcessor scheduleProcessor;

    @Autowired
    private RoomProcessor roomProcessor;

    @Autowired
    private ScheduleService scheduleService;


    @Before
    public void init() throws IOException {
        processRoomsForForeignKey();
    }

    @Test
    public void canProcessSchedule() throws IOException {
        processRooms();
        Assert.assertTrue(true);
    }

    @Test
    public void scheduleIsInDatabase() throws IOException {
        processRooms();
        LocalDateTime localDate = LocalDateTime.of(2018, 11, 12, 0, 0);
        Optional<Schedule> schedule = scheduleService.loadById(localDate.toLocalDate());
        Assert.assertTrue(schedule.isPresent());

    }

    private void processRooms() throws IOException {
        String textForObject = readFromClasspath("list-of-room.json");
        List<Room> rooms = objectMapper.readValue(textForObject, new TypeReference<List<Room>>() {
        });
        scheduleProcessor.process(rooms);
    }

    private void processRoomsForForeignKey() throws IOException {
        String textForObject = readFromClasspath("roomsDto.json");
        RoomsDto roomsDto = objectMapper.readValue(textForObject, RoomsDto.class);
        roomProcessor.process(roomsDto);
    }

    private String readFromClasspath(String filename) {
        try (InputStream in = new ClassPathResource(filename, getClass()).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to read file from classpath %s", filename), e);
        }
    }

}
