package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.model.Talk;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TalkProcessorTest {

    @Autowired
    private TalkProcessor talkProcessor;

    @Test
    public void testProcessWithEmptyList() {
        List<SlotDto> slotDtos = new ArrayList<>();
        List<Talk> talks = talkProcessor.process(slotDtos);
        Assert.assertTrue(talks.isEmpty());
    }
}
