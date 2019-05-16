package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.model.Speaker;
import be.xplore.conference.model.Talk;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
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
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TalkConverterTest {

    @Autowired
    private ObjectMapper objectMapper;

    private Speaker speaker;
    private List<SlotDto> slotDtoObjects;
    private List<Speaker> speakers;

    @Before
    public void init() throws IOException {
        speaker = Speaker.builder().uuid("9a0ff10f-a0f3-4366-8cba-647feaaa5587")
                .firstName("John").lastName("Doe")
                .avatarUrl("avatarurl").twitter("twitter")
                .etag("123-a").build();
        speakers = new ArrayList<>();
        speakers.add(speaker);
        String textForObject = readFromClasspath("slots.json");
        slotDtoObjects = objectMapper.readValue(textForObject, new TypeReference<List<SlotDto>>() {
        });
    }

    @Test
    public void testConvertTalk() {
        Talk talk = TalkConverter.toTalk(slotDtoObjects.get(0), speakers);
        Assert.assertNotNull(talk);
        Assert.assertEquals(speaker, talk.getSpeakers().get(0));
    }

    private String readFromClasspath(String filename) {
        try (InputStream in = new ClassPathResource(filename, getClass()).getInputStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Unable to read file from classpath %s %s", filename, e.getMessage()), e);
        }
    }
}
