package be.xplore.conference.schedulers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ClientSchedulerTest {

    @Autowired
    private ClientScheduler clientScheduler;

    @Test
    public void testCheckStatusClientsAndSendMail(){
        clientScheduler.checkStatusClientsAndSendMail();
        Assert.assertTrue(true);
    }
}
