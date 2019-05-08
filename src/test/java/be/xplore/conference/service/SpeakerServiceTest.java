package be.xplore.conference.service;

import be.xplore.conference.model.Speaker;
import org.junit.Assert;
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

    @Test
    public void testSaveSpeaker(){
        Speaker speaker = new Speaker("uuidfortesting","tester","tester","/*","twitter","bla");
        Speaker savedSpeaker = service.save(speaker);
        Assert.assertNotNull(savedSpeaker);
        Assert.assertEquals(speaker,savedSpeaker);
    }

    @Test
    public void testLoadSpeakerById(){
        Optional<Speaker> savedSpeaker = service.loadById("e14ef4212ffd330e97f9a0ff3420fc27714d4b0d");
        Assert.assertTrue(savedSpeaker.isPresent());
        Assert.assertEquals(savedSpeaker.get().getUuid(),"e14ef4212ffd330e97f9a0ff3420fc27714d4b0d");
        Assert.assertEquals(savedSpeaker.get().getLastName(),"Paumard");
    }
}
