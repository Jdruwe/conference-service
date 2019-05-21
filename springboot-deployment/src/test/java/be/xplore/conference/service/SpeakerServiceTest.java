package be.xplore.conference.service;

import be.xplore.conference.model.Speaker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SpeakerServiceTest {

    @Autowired
    private SpeakerService service;

    @Autowired
    private SpeakerService speakerService;

    private Speaker speaker;

    @Before
    public void init() {
        speaker = Speaker.builder()
                .uuid("9a0ff10f-a0f3-4366-8cba-647feaaa5587")
                .firstName("John")
                .lastName("Doe")
                .avatarUrl("avatarurl")
                .twitter("twitter")
                .etag("123-a").build();
        speakerService.save(speaker);
    }

    @Test
    public void testSaveSpeaker() {
        Speaker speaker = Speaker.builder().uuid("uuidfortesting").firstName("tester").lastName("tester")
                .avatarUrl("/*").twitter("twitter").etag("bla").build();

        Speaker savedSpeaker = service.save(speaker);
        Assert.assertNotNull(savedSpeaker);
        Assert.assertEquals(speaker, savedSpeaker);
    }

    @Test
    public void testLoadSpeakerById() {
        Optional<Speaker> savedSpeaker = service.loadById("9a0ff10f-a0f3-4366-8cba-647feaaa5587");
        Assert.assertTrue(savedSpeaker.isPresent());
        Assert.assertEquals(speaker, savedSpeaker.get());
    }
}
