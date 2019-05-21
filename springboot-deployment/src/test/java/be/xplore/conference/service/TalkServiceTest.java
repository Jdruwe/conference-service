package be.xplore.conference.service;

import be.xplore.conference.model.Talk;
import org.junit.Assert;
import org.junit.Before;
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

    private Talk savedTalk;

    @Before
    public void init() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Talk talkToSafe = Talk.builder()
                .id("CFL-7665")
                .startTime(localDateTime.minusDays(15))
                .endTime(localDateTime.minusDays(13))
                .fromTime("fromtime")
                .toTime("totime")
                .title("title")
                .type("type")
                .summary("summary")
                .speakers(null).build();
        savedTalk = talkService.save(talkToSafe);
    }

    @Test
    public void testSaveTalk() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Talk talk = talkService.save(new Talk("testTalk", localDateTime.minusDays(5), localDateTime.minusDays(3), "fromtime", "totime", "title", "type", "summary", null));
        Assert.assertNotNull(talk);
        Assert.assertEquals("testTalk", talk.getId());
    }

    @Test
    public void testLoadByNonExistingID() {
        Optional<Talk> talk = talkService.loadById("noId");
        Assert.assertTrue(talk.isEmpty());
    }

    @Test
    public void testLoadById() {
        Optional<Talk> talk = talkService.loadById("CFL-7665");
        Assert.assertTrue(talk.isPresent());
        Assert.assertEquals(savedTalk, talk.get());
    }
}
