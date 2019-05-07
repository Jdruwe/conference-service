package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.model.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RoomProcessorTest {

    @Autowired
    private RoomProcessor roomProcessor;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void processRooms() throws IOException {
        String textForObject = readFromClasspath("roomsDto.json");
        RoomsDto myObjects = objectMapper.readValue(textForObject, RoomsDto.class);

        List<Room> process = roomProcessor.process(myObjects);
        assertThat(process).isNotNull().satisfies(p -> {
            assertThat(p).hasSize(11);
            Assert.assertEquals("Room 8", p.get(0).getName());
            Assert.assertEquals("Room 5", p.get(1).getName());
            Assert.assertEquals("Room 4", p.get(10).getName());
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
