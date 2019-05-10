package be.xplore.conference.service;

import be.xplore.conference.model.Talk;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TalkServiceTest {

    @Autowired
    private TalkService talkService;

    @Test
    public void testSaveSetting() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Talk talk = talkService.save(new Talk("testTalk", localDateTime.minusDays(5), localDateTime.minusDays(3), "fromtime", "totime", "title", "type", "summary", null));
        Assert.assertNotNull(talk);
        Assert.assertEquals("testTalk", talk.getId());
    }

    @Test
    public void testLoadByNonExistingID() {
        Optional<Talk> talk = talkService.loadById("non");
        Assert.assertTrue(talk.isEmpty());
    }

    @Test
    public void testLoadById() {
        Optional<Talk> talk = talkService.loadById("CFL-7665");
        Assert.assertTrue(talk.isPresent());
        Assert.assertEquals("Lambdas and Streams Master Class Part 1",talk.get().getTitle());
        Assert.assertEquals("Deep Dive",talk.get().getType());
    }
}
